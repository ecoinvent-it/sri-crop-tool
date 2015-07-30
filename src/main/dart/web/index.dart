import 'dart:html';

import 'package:angular/angular.dart';
import 'package:angular/application_factory.dart';

import 'package:alcig/api/api.dart';
import 'package:alcig/api/api_impl.dart';
import 'package:alcig/menu_with_selection.dart';
import 'package:alcig/api/local_notification_service.dart';
import 'package:alcig/api/user.dart';
//import 'package:alcig/custom_annotations.dart';
import 'package:alcig/betaUserInfo/beta_user_info.dart';
import 'package:alcig/contactPage/contact_page.dart';
import 'package:alcig/notificationModal/notification_modal.dart';
import 'package:alcig/processGenerationSteps/process_generation_steps.dart';
import 'package:alcig/toolPage/tool_page.dart';

void main() {
  applicationFactory()
      .addModule(new MyAppModule())
      .run();
  window.postMessage('Dart is running', '*');
}

class MyAppModule extends Module {
  MyAppModule() {
    bind(RouteInitializerFn, toValue: alcigRouteInitializer);
    bind(NgRoutingUsePushState, toValue: new NgRoutingUsePushState.value(false));
    bind(MenuWithSelection);
    bind(Api, toImplementation: ApiImpl);
    bind(LocalNotificationService);
    bind(ProcessGeneratorSteps);
    bind(NotificationModal);
    bind(User, toValue: new User());
    bind(BetaUserInfo);
    bind(ToolPage);
    bind(ContactPage);
    // NOTE: This doesn't work, we had some issue with null value
    //bind(String, toValue: new Random().nextInt(1<<31).toString(), withAnnotation: const IdTab() );
  }
}

void alcigRouteInitializer(Router router, RouteViewFactory views) {
  views.configure({
    'tool': ngRoute(
        defaultRoute: true,
        path: 'tool',
        viewHtml: '<tool-page></tool-page>'),
    'contactUs': ngRoute(
        path: 'contactUs',
        viewHtml: '<contact-page></contact-page>')
  });
}