library license.api.impl;

import 'dart:async';
import 'dart:convert';
import 'dart:html';

import 'package:alcig/connectivity_state.dart';
import 'package:angular/di.dart';

import 'license_api.dart';

@Injectable()
class LicenseApiImpl implements LicenseApi {

  String _serverUrl;

  String get _baseUrl => _serverUrl + "app/";

  String get _baseLicenseApiUrl => _baseUrl + "api/license/";

  ConnectivityState _connectivityState;

  LicenseApiImpl(ConnectivityState this._connectivityState, String this._serverUrl);

  Future<List> getUserLicenses() async
  {
    try
    {
      String result = await HttpRequest.getString(
              _baseLicenseApiUrl + "userLicenses", withCredentials: _baseLicenseApiUrl.startsWith("http"));

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