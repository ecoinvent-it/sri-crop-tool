library user_page;

import 'package:angular/angular.dart';
import 'package:alcig/login/login_service.dart';

@Component(
    selector: 'params-page',
    templateUrl: 'packages/alcig/parametersPage/parameters_page.html',
    useShadowDom: false)
class ParametersPage
{
  LoginService _loginService;
  
  bool get isLogged => _loginService.isLogged;
  
  ParametersPage(LoginService this._loginService);
}