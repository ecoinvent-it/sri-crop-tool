library user_page;

import 'package:alcig/login/changePasswordBox/change_password_box.dart';
import 'package:alcig/login/login_service.dart';
import 'package:angular/angular.dart';

@Component(
        selector: 'params-page',
        directives: const [NgIf, ChangePasswordBox],
        templateUrl: 'parameters_page.html')
class ParametersPage
{
    LoginService _loginService;

    bool get isLogged => _loginService.isLogged;

    ParametersPage(LoginService this._loginService);
}