library login.reset_password_box;

import 'dart:async';

import 'package:alcig/login/login_service.dart';
import 'package:angular/angular.dart';
import 'package:angular_forms/angular_forms.dart';
import 'package:angular_router/angular_router.dart';

@Component(
        selector: 'reset-password-box',
        directives: const [NgIf, formDirectives],
        styleUrls: const ['reset_password_box.css'],
        templateUrl: 'reset_password_box.html')
class ResetPasswordBox
        implements OnInit, OnDestroy
{
    bool resetDone = false;

    String _validationCode;
    String newPassword = "";
    String newPasswordConfirmation = "";

    String errorMessage = "";
    String successMessage = "";

    bool isLoading;
    bool displayHomePageLink = false;

    LoginService _loginService;
    StreamSubscription _userSubscription;

    ResetPasswordBox(LoginService this._loginService, RouteParams params)
    {
        _validationCode = params.get('validationCode');
    }

    @override
    void ngOnInit()
    {
        isLoading = _loginService.isLoading;
        _userSubscription = _loginService.stream.listen(_onUserData);
    }

    @override
    void ngOnDestroy()
    {
        _userSubscription.cancel();
        _userSubscription = null;
    }

    void _onUserData(LoginEvent event)
    {
        isLoading = _loginService.isLoading;
        switch (event)
        {
            case LoginEvent.PASSWORD_RESET:
                resetDone = true;
                successMessage = "Your password has been changed.";
                displayHomePageLink = true;
                break;
            case LoginEvent.INVALID_NEW_PASSWORD:
                errorMessage = "Invalid new password";
                break;
            case LoginEvent.WRONG_VALIDATION_CODE:
                errorMessage = "This link is not valid. Did you already reset your password?";
                displayHomePageLink = true;
                break;
            case LoginEvent.EXPIRED_VALIDATION_CODE:
                errorMessage =
                "This link has expired. Please start again by clicking on ''Forgot password'' in the home page.";
                displayHomePageLink = true;
                break;
            default:
                break;
        }
    }

    void submit()
    {
        successMessage = "";
        errorMessage = "";
        displayHomePageLink = false;

        if (newPassword != newPasswordConfirmation)
            errorMessage = "The new password and the confirmation are not the same";
        else if (!_newPasswordIsValid())
            errorMessage = "Your password must have between 8 and 160 characters";
        else
            _loginService.resetPassword(_validationCode, newPassword);
    }

    bool _newPasswordIsValid()
    {
        return newPassword.length >= 8 && newPassword.length <= 160;
    }
}