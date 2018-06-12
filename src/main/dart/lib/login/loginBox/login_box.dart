library login.login_box;

import 'dart:async';
import 'dart:js' as js;

import 'package:alcig/login/forgotPasswordBox/forgot_password_box.dart';
import 'package:alcig/login/login_service.dart';
import 'package:alcig/login/registrationRequestBox/registration_request_box.dart';
import 'package:angular/angular.dart';
import 'package:angular_forms/angular_forms.dart';

@Component(
        selector: 'login-box',
        directives: const [formDirectives, NgIf, ForgotPasswordBox, RegistrationRequestBox],
        styleUrls: const ['login_box.css'],
        templateUrl: 'login_box.html')
class LoginBox
        implements OnInit, OnDestroy
{
    bool isLogged = false;
    bool isLoading = false;
    String errorMessage = "";
    String username = "";
    String password = "";

    final String idRegistrationRequestModal = "#registrationRequestModal";
    final String idForgotPasswordModal = "#forgotPasswordModal";
    bool isRegistrationRequestModalOpened = false;
    bool isForgotPwdModalOpened = false;

    LoginService _loginService;
    StreamSubscription _userSubscription;

    LoginBox(LoginService this._loginService);

    @override
    void ngOnInit() {
        isLogged = _loginService.isLogged;
        isLoading = _loginService.isLoading;
        _userSubscription = _loginService.stream.listen(_onUserData);
    }

    @override
    void ngOnDestroy() {
        _userSubscription.cancel();
        _userSubscription = null;
    }

    void _onUserData(LoginEvent event) {
        isLogged = _loginService.isLogged;
        isLoading = _loginService.isLoading;
        switch (event)
        {
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
            case LoginEvent.NON_ACTIVATED_USER:
                errorMessage = "Your account is not activated. Please check your emails";
                break;
            case LoginEvent.SERVER_ERROR:
                errorMessage = "Some connectivity issues occured. Please try again later";
                break;
            case LoginEvent.AUTHENTICATING:
                errorMessage = "";
                break;
            case LoginEvent.LOGGING_OUT:
                username = ""; //TODO: Should we add a message?
                break;
            case LoginEvent.LOGGED_OUT:
                username = ""; //TODO: Should we add a message?
                break;
            case LoginEvent.LOG_OUT_UNSURE:
                username = ""; //TODO: Should we add a message?
                break;
            case LoginEvent.PASSWORD_RESET:
                new Timer(new Duration(seconds: 2), () => _hideModal(idForgotPasswordModal));
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

    void openForgotPwdPopUp()
    {
        isForgotPwdModalOpened = true;
        _displayModal(idForgotPasswordModal);
        _listenModalClosing(idForgotPasswordModal, _onModalClosed);
    }

    void openRegistrationRequestPopUp() {
        isRegistrationRequestModalOpened = true;
        _displayModal(idRegistrationRequestModal);
        _listenModalClosing(idRegistrationRequestModal, _onModalClosed);
    }

    void _onModalClosed(e)
    {
        isForgotPwdModalOpened = false;
        isRegistrationRequestModalOpened = false;
    }

    void _hideModal(String id)
    {
        js.context.callMethod(r'$', [id])
                .callMethod('modal', ['hide']);
    }

    void _displayModal(String id)
    {
        js.context.callMethod(r'$', [id])
                .callMethod('modal', [new js.JsObject.jsify({'show': 'true'})]);
    }

    void _listenModalClosing(String id, Function callback)
    {
        // NOTE: Must be done when modal is displayed, otherwise it doesn't work
        js.context.callMethod(r'$', [id])
                .callMethod('on', ['hidden.bs.modal', callback]);
    }
}