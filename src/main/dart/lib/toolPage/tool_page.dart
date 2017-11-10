library tool_page;

import 'dart:html';

import 'package:alcig/login/loginBox/login_box.dart';
import 'package:alcig/login/userBox/user_box.dart';
import 'package:alcig/notificationModal/notification_modal.dart';
import 'package:alcig/processGenerationSteps/process_generation_steps.dart';
import 'package:angular/angular.dart';
import 'package:angular_router/angular_router.dart';

@Component(
        selector: 'tool-page',
        directives: const [LoginBox, UserBox, NotificationModal, ProcessGeneratorSteps],
        styleUrls: const ['tool_page.css'],
        templateUrl: 'tool_page.html')
class ToolPage
{
    Router _router;

    ToolPage(Router this._router);

    void goToFaq()
    {
        _router.navigate(["../Faq"]);
    }

    void goToEcoinvent()
    {
        window.location.href = 'http://www.ecoinvent.org/';
    }
}