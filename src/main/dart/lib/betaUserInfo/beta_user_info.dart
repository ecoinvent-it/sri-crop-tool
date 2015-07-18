library alcig.beta_user_info;

import 'package:angular/angular.dart';
import 'package:alcig/api/user.dart';

@Component(
    selector: 'beta-user-info',
    templateUrl: 'packages/alcig/betaUserInfo/beta_user_info.html',
    useShadowDom: false)
class BetaUserInfo
{
  User _user;
  
  String get userEmail => _user.email;
  set userEmail(String email) => _user.email = email;
  
  String get userName => _user.name;
  set userName(String name) => _user.name = name;
  
  String get userAddress => _user.address;
  set userAddress(String address) => _user.address = address;
  
  BetaUserInfo(User this._user);
}