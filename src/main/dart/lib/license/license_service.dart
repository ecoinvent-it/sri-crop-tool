library license.service;

import 'dart:async';
import 'package:di/annotations.dart';
import 'package:alcig/date_utils.dart';
import 'package:alcig/license/license_api.dart';
import 'package:alcig/login/login_service.dart';

@Injectable()
class LicenseService {
  
  List licenses = null;
  Map currentLicense = null;
  
  LicenseApi _api;
  LoginService _loginService;
    
  LicenseService(LicenseApi this._api, LoginService this._loginService)
  {
    if (_loginService.isLogged)
      _loadLicenses();
    _loginService.stream.listen(_onUserData);
  }
  
  void _onUserData(LoginEvent event) 
  {
    switch (event) 
    {
      case LoginEvent.AUTHENTICATED:
        _loadLicenses();
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
  
  Future _loadLicenses() async
  {
    List tempLicenses = await _api.getUserLicenses();
    tempLicenses.forEach((l) => l['endDateAsDate'] = DateUtils.parseDate(l['endDate']));
    licenses = tempLicenses;
    currentLicense = _findActiveLicense();
  }
  
  Map _findActiveLicense()
  {
    if (licenses.isEmpty)
      return null;
    try
    {
      return licenses.lastWhere((l) => !l['isDepleted']);
    }
    catch(e)
    {
      return licenses.first;
    }
  }
  
  int getNbGenerationsForLicense(Map license)
  {
    return license == null ? null : license['numberOfGenerations'];
  }
  
  bool hasUnlimitedUses(Map license)
  {
    if (license == null)
          return false;
    switch(license['rentalItem'])
    {
      case 'DEMO_UNLIMITED':
      case 'ACADEMIC_UNLIMITED':
      case 'BUSINESS_UNLIMITED':
        return true;
      default:
        return false;
    }
  }
  
  void updateLicenseDepletion(Map license)
  {
    license['isDepleted'] = true;
  }
  
  String displayRentalItem(Map license)
  {
    if (license == null)
      return null;
    switch(license['rentalItem'])
    {
      case 'DEMO_3_USES':
        return "Demo - 3 uses";
      case 'DEMO_UNLIMITED':
        return "Demo - unlimited uses";
      case 'ACADEMIC_10_USES':
        return "Academic - 10 uses";
      case 'ACADEMIC_25_USES':
        return "Academic - 25 uses";
      case 'ACADEMIC_50_USES':
        return "Academic - 50 uses";
      case 'ACADEMIC_UNLIMITED':
        return "Academic - unlimited uses";
      case 'BUSINESS_10_USES':
        return "Business - 10 uses";
      case 'BUSINESS_25_USES':
        return "Business - 25 uses";
      case 'BUSINESS_50_USES':
        return "Business - 50 uses";
      case 'BUSINESS_UNLIMITED':
        return "Business - unlimited uses";
      default:
        throw new Error();
    }
  }
  
  void _resetAll()
  {
    licenses = null;
    currentLicense = null;
  }
}