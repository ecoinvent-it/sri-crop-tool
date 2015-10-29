library generation.service;

import 'dart:async';
import 'dart:convert';
import 'dart:html';
import 'package:di/annotations.dart';
import 'package:alcig/api/api.dart';
import 'package:alcig/login/login_service.dart';

@Injectable()
class GenerationService {
  
  Api _api;
  Map lastGeneration;
  String get lastGenerationId => lastGeneration == null ? null : lastGeneration['id']['representation'];
  List generations = null;
  
  LoginService _loginService;
  
  GenerationService(Api this._api, LoginService this._loginService)
  {
    _loginService.stream.listen(_onUserData);
  }
  
  void _onUserData(LoginEvent event) 
  {
    switch ( event) 
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
  
  Future<Map> upload(String filename, FormData formData) async
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
      changeSelectedGeneration(newGeneration);
      return newGeneration;
    }
  }
  
  void changeSelectedGeneration(Map generation)
  {
    lastGeneration = generation;
  }
  
  void _resetAll()
  {
    lastGeneration = null;
    generations = null;
  }
}