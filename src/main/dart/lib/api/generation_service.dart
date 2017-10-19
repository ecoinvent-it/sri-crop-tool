library generation.service;

import 'dart:async';
import 'dart:convert';
import 'dart:html';

import 'package:alcig/api/api.dart';
import 'package:alcig/license/license_service.dart';
import 'package:alcig/login/login_service.dart';
import 'package:angular/di.dart';

@Injectable()
class GenerationService {

  Api _api;
  Map lastGeneration;
  String get lastGenerationId => lastGeneration == null ? null : lastGeneration['id']['representation'];
  List generations = null;

  LoginService _loginService;
  LicenseService _licenseService;

  //FIXME: Ugly
  String get serverUrl => _api.serverUrl;

  GenerationService(Api this._api, LoginService this._loginService, LicenseService this._licenseService)
  {
    if (_loginService.isLogged)
      _loadGenerations();
    _loginService.stream.listen(_onUserData);
  }

  void _onUserData(LoginEvent event)
  {
    switch (event)
    {
      case LoginEvent.AUTHENTICATED:
        _loadGenerations();
        break;
      case LoginEvent.LOGGED_OUT:
      case LoginEvent.LOG_OUT_UNSURE:
      case LoginEvent.LOG_OUT_BY_SERVER:
        _resetAll();
        break;
      default:
        break;
    }
  }

  Future _loadGenerations() async
  {
    generations = await _api.getUserGenerations();
  }

  Future<Map> upload(String filename, FormData formData, Map license) async
  {
    Map uploadedGeneration = lastGeneration;
    HttpRequest request = await _api.uploadInputs(formData);

    if ( request.status == 400 )
      return JSON.decode(request.responseText);
    else
    {
      Map newGeneration = JSON.decode(request.responseText);
      if (uploadedGeneration != null)
      {
        if (uploadedGeneration['id']['representation'] == newGeneration['id']['representation'])
          generations.remove(uploadedGeneration);
      }
      generations.insert(0, newGeneration);
      if (getNbRemainingGenerationsForLicense(generations, license) == 0)
        _licenseService.updateLicenseDepletion(license);
      changeSelectedGeneration(newGeneration);
      return newGeneration;
    }
  }

  void changeSelectedGeneration(Map generation)
  {
    lastGeneration = generation;
  }

  int getNbRemainingGenerationsForLicense(List generations, Map license)
  {
    int total = _licenseService.getNbGenerationsForLicense(license);
    if (total == null)
      return null;
    int nbUsed = _getNbUseGenerationsForLicense(generations, license);
    return total - nbUsed;
  }

  int _getNbUseGenerationsForLicense(List generations, Map license)
  {
    if (generations == null)
      return 0;
    int nb = 0;
    generations.forEach((g) {if (g['licenseId']['representation'] == license['id']['representation']) nb++;});
    return nb;
  }

  void _resetAll()
  {
    lastGeneration = null;
    generations = null;
  }
}