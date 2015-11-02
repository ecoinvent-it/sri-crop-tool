library license.api.impl;

import 'dart:async';
import 'dart:convert';
import 'dart:html';
import 'license_api.dart';
import 'package:di/annotations.dart';
import 'package:alcig/connectivity_state.dart';

@Injectable()
class LicenseApiImpl implements LicenseApi {
  static const String _baseUrl = "app/";
  static final String _baseLicenseApiUrl = _baseUrl + "api/license/";
  
  ConnectivityState _connectivityState;
  
  LicenseApiImpl(ConnectivityState this._connectivityState);
  
  Future<List> getUserLicenses() async
  {
    try
    {
      String result = await HttpRequest.getString(_baseLicenseApiUrl + "userLicenses");
      return JSON.decode(result); 
    }
    catch(e)
    {
      _manageError(e);
    }
  }
  
  void _manageError(ProgressEvent e)
  {
    _connectivityState.loggedOut();
    HttpRequest request = e.target;
    throw request.status;
  }
}