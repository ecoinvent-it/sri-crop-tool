library tool_page;

import 'package:alcig/login/loginBox/login_box.dart';
import 'package:alcig/login/userBox/user_box.dart';
import 'package:alcig/notificationModal/notification_modal.dart';
import 'package:alcig/processGenerationSteps/process_generation_steps.dart';
import 'package:angular/angular.dart';

@Component(
        selector: 'tool-page',
        directives: const [LoginBox, UserBox, NotificationModal, ProcessGeneratorSteps],
        templateUrl: 'tool_page.html')
class ToolPage
{
    ToolPage();
}