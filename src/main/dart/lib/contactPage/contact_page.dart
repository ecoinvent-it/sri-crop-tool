library contact_page;

import 'dart:async';
import 'dart:html';

import 'package:alcig/api/api.dart';
import 'package:angular/angular.dart';
import 'package:angular_forms/angular_forms.dart';

@Component(
        selector: 'contact-page',
        directives: const [formDirectives],
        templateUrl: 'contact_page.html')
class ContactPage
{
    Api _api;

    String contactEmail = "";
    String contactName = "";
    String contactCompany = "";
    String confirmationMessage = "";

    String quantisEmail = "software-support@quantis-intl.com";

    ContactPage(Api this._api);

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