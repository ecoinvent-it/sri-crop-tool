library login.login_box;

import 'dart:async';
import 'package:angular/angular.dart';
import 'package:alcig/login/login_service.dart';

@Component(
    selector: 'login-box',
    templateUrl: 'packages/alcig/login/loginBox/login_box.html',
    useShadowDom: false)
class LoginBox implements AttachAware, DetachAware
{
   bool isLogged = false;
   bool isLoading = false;
   String errorMessage = "";
   String username = "";
   String password = "";
  
  LoginService _loginService;
  StreamSubscription _userSubscription;
  
  LoginBox(LoginService this._loginService);
  
  void attach() {
    isLogged = _loginService.isLogged;
    isLoading = _loginService.isLoading;
    _userSubscription = _loginService.stream.listen(_onUserData);
  }
  
  void detach() {
    _userSubscription.cancel();
    _userSubscription = null;
  }
  
  void _onUserData(LoginEvent event) {
    isLogged = _loginService.isLogged;
    isLoading = _loginService.isLoading;
    switch ( event) {
      case LoginEvent.AUTHENTICATED:
        errorMessage = "";
        username = _loginService.username;
        password = "";
        break;
      case LoginEvent.WRONG_CREDENTIALS:
        errorMessage = "Wrong user or password";
        break;
      case LoginEvent.LOCKED_USER:
        errorMessage = "Your account has been locked";
        break;
      case LoginEvent.NON_VALIDATED_USER:
        errorMessage = "Your account is not validated. Please check your emails";
        break;
      case LoginEvent.SERVER_ERROR:
        errorMessage = "Some connectivity issues occured. Please try again later";
        break;
      case LoginEvent.AUTHENTICATING:
        errorMessage = "";
        break;
      case LoginEvent.LOGGING_OUT:
        username = "";//TODO: Should we add a message?
        break;
      case LoginEvent.LOGGED_OUT:
        username = "";//TODO: Should we add a message?
        break;
      case LoginEvent.LOG_OUT_UNSURE:
        username = "";//TODO: Should we add a message?
        break;
      case LoginEvent.LOG_OUT_BY_SERVER:
        errorMessage = "Your session have been reinitialized. Please log in again";
        break;
      default:
        break;
    }
  }
  
  void loginSubmit() {
    _loginService.login(username, password);
  }
  
  void logout() {
    _loginService.logout();
  }
}
