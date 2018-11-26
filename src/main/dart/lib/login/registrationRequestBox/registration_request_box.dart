import 'dart:html';

import 'package:alcig/api/api.dart';
import 'package:angular/angular.dart';
import 'package:angular_forms/angular_forms.dart';

@Component(
    selector: 'registration-request-box',
    directives: const [formDirectives],
    styleUrls: const ['registration_request_box.css'],
    templateUrl: 'registration_request_box.html')
class RegistrationRequestBox {
  Api _api;
  String name = "";
  String username = "";
  String company = "";
  String address = "";
  String mail = "";
  bool requestSent = false;

  RegistrationRequestBox(Api this._api);

  void submitRequest(Event e) {
    FormElement form = e.currentTarget;
    FormData formData = new FormData(form);
    _api.sendRequest(formData).whenComplete(() => requestSent = true);
  }
}