library login.change_password_box;

import 'dart:async';
import 'package:angular/angular.dart';
import 'package:alcig/login/login_service.dart';

@Component(
    selector: 'change-password-box',
    templateUrl: 'packages/alcig/login/changePasswordBox/change_password_box.html',
    useShadowDom: false)
class ChangePasswordBox implements AttachAware, DetachAware
{
  String oldPassword;
  String newPassword;
  String newPasswordConfirmation;
  
  bool isLoading = false;
  bool forceChangePassword;
  String errorMessage = "";
  String successMessage = "";
  
  LoginService _loginService;
  StreamSubscription _userSubscription;
  
  ChangePasswordBox(LoginService this._loginService);
  
  void attach() 
  {
    isLoading = _loginService.isLoading;
    forceChangePassword = _loginService.forceChangePassword;
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
    forceChangePassword = _loginService.forceChangePassword;
    successMessage = "";
    switch ( event) {
      case LoginEvent.PASSWORD_CHANGED:
        _resetFields();
        successMessage = "Your password has been changed.";
        break;
      case LoginEvent.INVALID_NEW_PASSWORD:
        errorMessage = "The new password is not valid";
        break;
      case LoginEvent.WRONG_CURRENT_PASSWORD:
        errorMessage = "Wrong old password";
        break;
      default:
        break;
    }
  }
  
  void changePassword()
  {
    if ( newPassword != newPasswordConfirmation )
      errorMessage = "The new password and the confirmation are not the same";
    else if ( !_newPasswordIsValid() )
      errorMessage = "Your password must have between 8 and 160 characters";
    else
    {
      _loginService.changePassword(oldPassword, newPassword);
    }
  }
  
  bool _newPasswordIsValid() 
  {
      return newPassword.length >= 8 && newPassword.length <= 160;
  }
  
  void logout()
  {
    _resetFields();
    _loginService.logout();
  }
  
  void _resetFields()
  {
    oldPassword = "";
    newPassword = "";
    newPasswordConfirmation = "";
    successMessage = "";
    errorMessage = "";
  }
}