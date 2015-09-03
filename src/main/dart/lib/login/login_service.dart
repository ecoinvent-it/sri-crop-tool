library login.service;

import 'dart:async';
import 'package:di/annotations.dart';
import 'package:alcig/login/login_api.dart';
import 'package:alcig/connectivity_state.dart';

@Injectable()
class LoginService {
  
  LoginApi _loginApi;
  
  StreamController<LoginEvent> _dispatcher = new StreamController.broadcast();
  Stream<LoginEvent> get stream => _dispatcher.stream;
  
  String _username;
    
  bool _isLoading = false;
  
  bool forceChangePassword = true;

  String get username => _username;
      
  bool get isLogged => _username != null;
  
  bool get isLoading => _isLoading;
  
  LoginService(LoginApi this._loginApi, ConnectivityState connectivityState){
    connectivityState.stream.listen( (StateEvent event) {
      if (event == StateEvent.NOT_AUTHED && isLogged) {
        _username = null;
        _dispatcher.add(LoginEvent.LOG_OUT_BY_SERVER);
      }
    });
  }
  
  Future login(String username, String password) async 
  {
    if ( !isLoading ) 
    {
      _isLoading = true;
      _dispatcher.add(LoginEvent.AUTHENTICATING);
    
      try
      {
        AuthRequestResult res = await _loginApi.login(username, password);
        switch ( res ) 
        {
           case AuthRequestResult.OK:
             _dispatcher.add(LoginEvent.AUTHENTICATED);
             _username = username;
             _getStatus();
             break;
           case AuthRequestResult.WRONG_CREDENTIALS:
             _dispatcher.add(LoginEvent.WRONG_CREDENTIALS);
             break;
           case AuthRequestResult.LOCKED_USER:
             _dispatcher.add(LoginEvent.LOCKED_USER);
             break;
           case AuthRequestResult.NON_ACTIVATED_USER:
             _dispatcher.add(LoginEvent.NON_ACTIVATED_USER);
             break;
         }
      }
      catch(e)
      {
        print("Server error: $e");
        _dispatcher.add(LoginEvent.SERVER_ERROR);
      }
      _isLoading = false;
    }
  }
  
  Future logout() async 
  {
    if ( ! isLoading ) 
    {
      _isLoading = true;
      _dispatcher.add(LoginEvent.LOGGING_OUT);

      try
      {
        await _loginApi.logout();
        _dispatcher.add(LoginEvent.LOGGED_OUT);
      }
      catch(e)
      {
        print("Server error: $e");
        _dispatcher.add(LoginEvent.LOG_OUT_UNSURE);
      }
      _isLoading = false;
      _username = null;
    }
  }
  
  Future _getStatus() async
  {
    try
    {
      StatusResult status = await _loginApi.getStatus();
      switch(status)
      {
        case StatusResult.OK:
          break;
        case StatusResult.MUST_CHANGE_PASSWORD:
          forceChangePassword = true;
          _dispatcher.add(LoginEvent.FORCE_PASSWORD_CHANGE);
      }
    }
    catch(e)
    {
      print("Server error: $e");
      _dispatcher.add(LoginEvent.LOG_OUT_UNSURE);
    }
  }
  
  Future changePassword(String oldPassword, String newPassword) async
  {
    if ( ! isLoading ) 
    {
      _isLoading = true;
      try
      {
        ChangePasswordResult result = await _loginApi.changePassword(oldPassword, newPassword);
        switch(result)
        {
          case ChangePasswordResult.OK:
            forceChangePassword = false;
            _dispatcher.add(LoginEvent.PASSWORD_CHANGED);
            break;
          case ChangePasswordResult.WRONG_PASSWORD:
            _dispatcher.add(LoginEvent.WRONG_CURRENT_PASSWORD);
            break;
          case ChangePasswordResult.INVALID_NEW_PASSWORD:
            _dispatcher.add(LoginEvent.INVALID_NEW_PASSWORD);
            break;
        }
      }
      catch(e)
      {
        print("Server error: $e");
        _dispatcher.add(LoginEvent.LOG_OUT_UNSURE);
      }
      _isLoading = false;
    }
  }
  
  Future forgotPassword(String email) async
  {
    if ( ! isLoading ) 
    {
      _isLoading = true;
      try
      {
        ForgotPasswordResult result = await _loginApi.forgotPassword(email);
        switch(result)
        {
          case ForgotPasswordResult.OK:
            _dispatcher.add(LoginEvent.VALIDATION_CODE_SENT);
            break;
          case ForgotPasswordResult.USER_ACTIVATION_PENDING:
            _dispatcher.add(LoginEvent.USER_ACTIVATION_PENDING);
            break;
        }
      }
      catch(e)
      {
        print("Server error: $e");
        _dispatcher.add(LoginEvent.LOG_OUT_UNSURE);
      }
      _isLoading = false;
    }
  }
  
  Future resetPassword(String email, String validationCode, String newPassword) async
    {
      if ( ! isLoading ) 
      {
        _isLoading = true;
        try
        {
          ResetPasswordResult result = await _loginApi.resetPassword(email, validationCode, newPassword);
          switch(result)
          {
            case ResetPasswordResult.OK:
              _dispatcher.add(LoginEvent.PASSWORD_RESET);
              break;
            case ResetPasswordResult.EXPIRED_VALIDATION_CODE:
              _dispatcher.add(LoginEvent.EXPIRED_VALIDATION_CODE);
              break;
            case ResetPasswordResult.INVALID_NEW_PASSWORD:
              _dispatcher.add(LoginEvent.INVALID_NEW_PASSWORD);
              break;
            case ResetPasswordResult.WRONG_VALIDATION_CODE:
              _dispatcher.add(LoginEvent.WRONG_VALIDATION_CODE);
              break;
          }
        }
        catch(e)
        {
          print("Server error: $e");
          _dispatcher.add(LoginEvent.LOG_OUT_UNSURE);
        }
        _isLoading = false;
      }
    }
}

enum LoginEvent {
  AUTHENTICATING,
  AUTHENTICATED,
  WRONG_CREDENTIALS,
  LOCKED_USER,
  NON_ACTIVATED_USER,
  SERVER_ERROR,
  LOGGING_OUT,
  LOGGED_OUT,
  LOG_OUT_UNSURE,
  LOG_OUT_BY_SERVER,
  FORCE_PASSWORD_CHANGE,
  PASSWORD_CHANGED,
  WRONG_CURRENT_PASSWORD,
  INVALID_NEW_PASSWORD,
  USER_ACTIVATION_PENDING,
  VALIDATION_CODE_SENT,
  PASSWORD_RESET,
  EXPIRED_VALIDATION_CODE,
  WRONG_VALIDATION_CODE
}