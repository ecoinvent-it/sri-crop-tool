library login.forgot_password_box;

import 'dart:async';
import 'dart:html';
import 'package:angular/angular.dart';
import 'package:alcig/login/login_service.dart';

@Component(
    selector: 'forgot-password-box',
    templateUrl: 'packages/alcig/login/forgotPasswordBox/forgot_password_box.html',
    useShadowDom: false)
class ForgotPasswordBox implements AttachAware, DetachAware
{
  int step = 0;
  
  String email = "";
  
  String validationCode = "";
  String newPassword = "";
  String newPasswordConfirmation = "";
  
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
        step = 1;
        break;
      case LoginEvent.USER_ACTIVATION_PENDING:
        errorMessage = "Your access is not activated yet. Please check your emails.";
        break;
      case LoginEvent.PASSWORD_RESET:
        step = 2;
        successMessage = "Your password has been changed.";
        break;
      case LoginEvent.INVALID_NEW_PASSWORD:
        errorMessage = "Invalid new password";
        break;
      case LoginEvent.WRONG_VALIDATION_CODE:
        errorMessage = "Wrong validation code";
        break;
      case LoginEvent.EXPIRED_VALIDATION_CODE:
        errorMessage = "Expired validation code. Please start again";
        break;
      default:
        break;
    }
  }
  
  void submit()
  {
    successMessage = "";
    errorMessage = "";
    if (step == 0)
    {
      _loginService.forgotPassword(email);
    }
    else
    {
      if ( newPassword != newPasswordConfirmation )
        errorMessage = "The new password and the confirmation are not the same";
      else if ( !_newPasswordIsValid() )
        errorMessage = "Your password must have at least 8 characters";
      else
        _loginService.resetPassword(email, validationCode, newPassword);
    }
  }
  
  bool _newPasswordIsValid() 
  {
      return newPassword.length >= 8;
  }
}