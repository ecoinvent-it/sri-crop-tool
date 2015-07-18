//import 'dart:math';

import 'package:angular/angular.dart';
import 'package:angular/application_factory.dart';

import 'package:alcig/api/api.dart';
import 'package:alcig/api/api_impl.dart';
import 'package:alcig/api/local_notification_service.dart';
import 'package:alcig/api/user.dart';
//import 'package:alcig/custom_annotations.dart';
import 'package:alcig/betaUserInfo/beta_user_info.dart';
import 'package:alcig/notificationModal/notification_modal.dart';
import 'package:alcig/process_generation_steps.dart';

void main() {
  applicationFactory()
      .addModule(new MyAppModule())
      .run();
}

class MyAppModule extends Module {
  MyAppModule() {
    bind(Api, toImplementation: ApiImpl);
    bind(LocalNotificationService);
    bind(ProcessGeneratorSteps);
    bind(NotificationModal);
    bind(User, toValue: new User());
    bind(BetaUserInfo);
    // NOTE: This doesn't work, we had some issue with null value
    //bind(String, toValue: new Random().nextInt(1<<31).toString(), withAnnotation: const IdTab() );
  }
}