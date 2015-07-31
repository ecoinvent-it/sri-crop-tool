library contact_page;

import 'dart:async';
import 'dart:html';
import 'package:angular/angular.dart';

import 'package:alcig/api/api.dart';
import 'package:alcig/api/user.dart';

@Component(
    selector: 'contact-page',
    templateUrl: 'packages/alcig/contactPage/contact_page.html',
    useShadowDom: false)
class ContactPage
{
  Api _api;
  User _user;
  
  String contactEmail = "";
  String contactName = "";
  String contactCompany = "";
  String confirmationMessage = "";
  
  String quantisEmail ="software-support@quantis-intl.com";
  
  ContactPage(Api this._api, User this._user)
  {
    contactEmail = _user.email;
    contactName = _user.name;
  }
 
  Future sendMessage(Event e) async
  {
    confirmationMessage = "";
    FormElement form = e.currentTarget;
    FormData formData = new FormData(form);
    await _api.contactUs(formData);
    form.reset();
    confirmationMessage = "Thank you! We have received your feedback and will get back to you as soon as possible.";
  }
  
}