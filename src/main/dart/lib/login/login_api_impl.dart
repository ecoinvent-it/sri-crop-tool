library login.api.impl;

import 'dart:async';
import 'dart:html';

import 'login_api.dart';
import 'package:di/annotations.dart';
import 'package:alcig/connectivity_state.dart';

@Injectable()
class LoginApiImpl implements LoginApi {
  static const String _baseUrl = "app/";
  static final String _basePrincipalUrl = _baseUrl + "api/principal/";
  static final String _basePubPrincipalUrl = _baseUrl + "api/pub/principal/";
  
  ConnectivityState _connectivityState;

  LoginApiImpl(ConnectivityState this._connectivityState);
  
  Future<AuthRequestResult> login(String username, String password) async 
  {
    Map<String, String> params = new Map();
    params["username"] = username;
    params["password"] = password;
    
    try
    {
      await HttpRequest.postFormData(_baseUrl + "login", params);
      _connectivityState..restoreOnline()..loggedIn();
      return AuthRequestResult.OK;
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
          case 'NON_ACTIVATED_USER':
            return AuthRequestResult.NON_ACTIVATED_USER;
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
  
  Future<StatusResult> getStatus() async
  {
    try
    {
      String response = await HttpRequest.getString(_basePrincipalUrl + "getStatus");
      switch(response)
      {
        case "true":
          return StatusResult.OK;
        case "false":
          return StatusResult.MUST_CHANGE_PASSWORD;
        default:
          throw response;
      }
    }
    catch(e)
    {
      _manageError(e);
    }
  }
  
  //TODO: In case of failure, we should retry
  Future<ChangePasswordResult> changePassword(String oldPassword, String newPassword) async
  {
    try
    {
      await  HttpRequest.postFormData(_basePrincipalUrl + "changePassword",
                                  {"oldPassword": oldPassword, "newPassword": newPassword});
      return ChangePasswordResult.OK;
    }
    catch(e)
    {
      HttpRequest request = e.target;
      if ( request.status == 400 ) 
      {
        switch (request.responseText) 
        {
          case 'WRONG_CURRENT_PASSWORD':
            return ChangePasswordResult.WRONG_PASSWORD;
          case 'INVALID_NEW_PASSWORD':
            return ChangePasswordResult.INVALID_NEW_PASSWORD;
          default:
            throw request;
        }
      }
      else
        _manageError(e);
    }
  }
  
  Future<ForgotPasswordResult> forgotPassword(String email) async
  {
    try
    {
      await  HttpRequest.postFormData(_basePubPrincipalUrl + "forgotPassword",
                                  {"email": email});
      return ForgotPasswordResult.OK;
    }
    catch(e)
    {
      HttpRequest request = e.target;
      if ( request.status == 400 ) 
      {
        switch (request.responseText) 
        {
          case 'USER_ACTIVATION_PENDING':
            return ForgotPasswordResult.USER_ACTIVATION_PENDING;
          default:
            throw request;
        }
      }
      else
        _manageError(e);
    }
  }
  
  Future<ResetPasswordResult> resetPassword(String email, String validationCode, String newPassword) async
  {
    Map params = new Map();
    params["email"] = email;
    params["validationCode"] = validationCode;
    params["newPassword"] = newPassword;
    try
    {
      await  HttpRequest.postFormData(_basePubPrincipalUrl + "resetPassword", params);
      return ResetPasswordResult.OK;
    }
    catch(e)
    {
      HttpRequest request = e.target;
      if ( request.status == 400 ) 
      {
        switch (request.responseText) 
        {
          case 'EXPIRED_VALIDATION_CODE':
            return ResetPasswordResult.EXPIRED_VALIDATION_CODE;
          case 'INVALID_NEW_PASSWORD':
            return ResetPasswordResult.INVALID_NEW_PASSWORD;
          case 'WRONG_VALIDATION_CODE':
            return ResetPasswordResult.WRONG_VALIDATION_CODE;
          default:
            throw request;
        }
      }
      else
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