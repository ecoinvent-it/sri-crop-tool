library login.forgot_password_box;

import 'dart:async';
import 'package:angular/angular.dart';
import 'package:alcig/login/login_service.dart';

@Component(
    selector: 'forgot-password-box',
    templateUrl: 'packages/alcig/login/forgotPasswordBox/forgot_password_box.html',
    useShadowDom: false)
class ForgotPasswordBox implements AttachAware, DetachAware
{
  bool sent = false;
  
  String email = "";
  
  String errorMessage = "";
  String successMessage = "";
  
  bool isLoading;
  
  LoginService _loginService;
  StreamSubscription _userSubscription;
    
  ForgotPasswordBox(LoginService this._loginService);
  
  void attach() 
  {
    isLoading = _loginService.isLoading;
    _userSubscription = _loginService.stream.listen(_onUserData);
  }
    
  void detach() 
  {
    _userSubscription.cancel();
    _userSubscription = null;
  }
  
  void _onUserData(LoginEvent event) 
  {
    isLoading = _loginService.isLoading;
    switch ( event) {
      case LoginEvent.VALIDATION_CODE_SENT:
        successMessage = "You will receive an email with all the information to reset your password.";
        sent = true;
        break;
      case LoginEvent.USER_ACTIVATION_PENDING:
        errorMessage = "Your access is not activated yet. Please check your emails.";
        sent = true;
        break;
      default:
        break;
    }
  }
  
  void submit()
  {
    successMessage = "";
    errorMessage = "";
    _loginService.forgotPassword(email);
  }

}