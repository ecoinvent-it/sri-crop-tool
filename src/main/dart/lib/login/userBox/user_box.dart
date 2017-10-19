library login.user_box;

import 'dart:async';
import 'dart:js' as js;

import 'package:alcig/login/changePasswordBox/change_password_box.dart';
import 'package:alcig/login/login_service.dart';
import 'package:angular/angular.dart';

@Component(
        selector: 'user-box',
        directives: const [NgIf, ChangePasswordBox],
        templateUrl: 'user_box.html')
class UserBox
        implements OnInit, OnDestroy
{
    bool isLoading = false;
    bool isLogged = false;
    bool forceChangePassword = false;

    String get username => _loginService.username == null ? "" : _loginService.username;

    LoginService _loginService;
    StreamSubscription _userSubscription;

    UserBox(LoginService this._loginService);

    @override
    void ngOnInit() {
        isLoading = _loginService.isLoading;
        isLogged = _loginService.isLogged;
        forceChangePassword = _loginService.forceChangePassword;
        _userSubscription = _loginService.stream.listen(_onUserData);
    }

    @override
    void ngOnDestroy() {
        _userSubscription.cancel();
        _userSubscription = null;
    }

    void _onUserData(LoginEvent event) {
        isLoading = _loginService.isLoading;
        isLogged = _loginService.isLogged;
        forceChangePassword = _loginService.forceChangePassword;
        switch (event)
        {
            case LoginEvent.AUTHENTICATED:
                break;
            case LoginEvent.FORCE_PASSWORD_CHANGE:
                _displayModal("#forceChangePwdModal");
                break;
            case LoginEvent.PASSWORD_CHANGED:
                new Timer(new Duration(seconds: 2), () => _hideModal("#forceChangePwdModal"));
                break;
            case LoginEvent.LOGGED_OUT:
            case LoginEvent.LOG_OUT_UNSURE:
            case LoginEvent.LOG_OUT_BY_SERVER:
                _hideModal("#forceChangePwdModal");
                break;
            default:
                break;
        }
    }

    void logout()
    {
        _loginService.logout();
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
}
