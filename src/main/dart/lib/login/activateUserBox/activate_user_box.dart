library login.activate_user_box;

import 'dart:async';

import 'package:alcig/login/login_api.dart';
import 'package:alcig/login/login_service.dart';
import 'package:angular/angular.dart';
import 'package:angular_forms/angular_forms.dart';
import 'package:angular_router/angular_router.dart';

@Component(
        selector: 'activate-user-box',
        directives: const [NgIf, formDirectives],
        styleUrls: const ['activate_user_box.css'],
        templateUrl: 'activate_user_box.html')
class ActivateUserBox
        implements OnInit, OnDestroy
{
    String _registrationCode;

    bool codeChecked = false;
    String checkCodeErrorMessage = "";

    String username = "";
    bool acceptTermsAndConditions = false;
    String newPassword = "";
    String newPasswordConfirmation = "";

    String errorMessage = "";
    String successMessage = "";

    bool activationDone = false;

    bool isLoading;

    LoginService _loginService;
    LoginApi _api;
    StreamSubscription _userSubscription;
    Router _router;

    ActivateUserBox(LoginService this._loginService, LoginApi this._api, Router this._router,
                    RouteParams params)
    {
        _registrationCode = params.get('registrationCode');
    }

    @override
    void ngOnInit()
    {
        _userSubscription = _loginService.stream.listen(_onUserData);
        _initPage();
    }

    @override
    void ngOnDestroy()
    {
        _userSubscription.cancel();
        _userSubscription = null;
    }

    Future _initPage() async
    {
        String responseText = await _api.checkRegistrationCode(_registrationCode);
        switch (responseText)
        {
            case 'EXPIRED_REGISTRATION_CODE':
                checkCodeErrorMessage = "This link has expired. Please contact your reseller to get a new valid link.";
                break;
            case 'WRONG_REGISTRATION_CODE':
                checkCodeErrorMessage = "This link is not valid. Did you already activate your account?";
                break;
            default:
                codeChecked = true;
                username = responseText;
                break;
        }
    }

    void _onUserData(LoginEvent event)
    {
        isLoading = _loginService.isLoading;
        switch (event)
        {
            case LoginEvent.AUTHENTICATED:
                new Timer (new Duration(seconds: 2), () => _router.navigate(["/Tool"]));
                break;
            default:
                break;
        }
    }

    void submit()
    {
        successMessage = "";
        errorMessage = "";

        if (newPassword != newPasswordConfirmation)
        {
            errorMessage = "The new password and the confirmation are not the same";
            return;
        }

        else if (!_newPasswordIsValid())
        {
            errorMessage = "Your password must have between 8 and 160 characters";
            return;
        }

        if (!acceptTermsAndConditions)
        {
            errorMessage = "You must accept the policy, terms and conditions";
            return;
        }
        _activateUser();
    }

    bool _newPasswordIsValid()
    {
        return newPassword.length >= 8 && newPassword.length <= 160;
    }

    Future _activateUser() async
    {
        isLoading = true;
        String responseText = await _api.activateUser(_registrationCode, acceptTermsAndConditions, newPassword);
        isLoading = false;
        switch (responseText)
        {
            case 'EXPIRED_REGISTRATION_CODE':
                errorMessage = "This link has expired. Please contact your Quantis seller to get a new valid link.";
                break;
            case 'WRONG_REGISTRATION_CODE':
                errorMessage = "This link is not valid. Did you already activate your account?";
                break;
            case 'INVALID_NEW_PASSWORD':
                errorMessage = "Invalid new password";
                break;
            default:
                {
                    activationDone = true;
                    successMessage =
                    "Your access is now activated! You will be automatically redirected to the home page.";
                    _loginService.login(responseText, newPassword);
                }
        }
    }

}