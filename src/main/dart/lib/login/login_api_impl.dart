library login.api.impl;

import 'dart:async';
import 'dart:html';

import 'login_api.dart';
import 'package:di/annotations.dart';
import 'package:alcig/connectivity_state.dart';

@Injectable()
class LoginApiImpl implements LoginApi {
  static const String _baseUrl = "app/";
  
  ConnectivityState _connectivityState;

  LoginApiImpl(ConnectivityState this._connectivityState);
  
  Future<AuthRequestResult> login(String username, String password) async 
  {
    Map<String, String> params = new Map();
    params["username"] = username;
    params["password"] = password;
    
    try
    {
      HttpRequest httpRequest = await HttpRequest.postFormData(_baseUrl + "login", params);
      _connectivityState..restoreOnline()..loggedIn();
      return httpRequest.responseText.contains("CHANGE_PASSWORD") ? 
              AuthRequestResult.OK_BUT_CHANGE_PASSWORD : AuthRequestResult.OK;
    }
    catch(e)
    {
      HttpRequest request = e.target;
      if ( request.status == 401 ) 
      {
        switch ( request.responseText ) 
        {
          case 'WRONG_CREDENTIALS':
            return AuthRequestResult.WRONG_CREDENTIALS;
          case 'LOCKED_USER':
            return AuthRequestResult.LOCKED_USER;
          case 'NON_VALIDATED_USER':
            return AuthRequestResult.NON_VALIDATED_USER;
          default:
            throw request;
        }
      }
      else //TODO: Test what happens in case of timeout
        throw request;
    }
  }
      
  //TODO: In case of failure, we should retry
  Future<AuthRequestResult> logout() async 
  {
    try
    {
      await HttpRequest.postFormData(_baseUrl + "logout", {});
      _connectivityState.loggedOut();
      return AuthRequestResult.OK;
    }
    catch(e)
    {
      HttpRequest request = e.target;
      if ( request.status == 401 )
      {
        _connectivityState.loggedOut();
        return AuthRequestResult.OK;
      }
      else
        throw request;
    }

  }
}