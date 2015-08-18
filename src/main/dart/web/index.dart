import 'dart:html';

import 'package:angular/angular.dart';
import 'package:angular/application_factory.dart';

import 'package:alcig/connectivity_state.dart';
import 'package:alcig/api/api.dart';
import 'package:alcig/api/api_impl.dart';
import 'package:alcig/api/local_notification_service.dart';

import 'package:alcig/menu_with_selection.dart';
import 'package:alcig/login/login_api.dart';
import 'package:alcig/login/login_api_impl.dart';
import 'package:alcig/login/login_service.dart';
import 'package:alcig/login/login_box.dart';
//import 'package:alcig/custom_annotations.dart';
import 'package:alcig/contactPage/contact_page.dart';
import 'package:alcig/notificationModal/notification_modal.dart';
import 'package:alcig/processGenerationSteps/process_generation_steps.dart';
import 'package:alcig/toolPage/tool_page.dart';
import 'package:alcig/faqPage/faq_page.dart';

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
    bind(ConnectivityState);
    bind(Api, toImplementation: ApiImpl);
    bind(LocalNotificationService);
    bind(ProcessGeneratorSteps);
    bind(NotificationModal);
    bind(ToolPage);
    bind(ContactPage);
    bind(FaqPage);
    bind(LoginApi, toImplementation: LoginApiImpl);
    bind(LoginService);
    bind(LoginBox);
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
        viewHtml: '<contact-page></contact-page>'),
    'faq': ngRoute(
            path: 'faq',
            viewHtml: '<faq-page></faq-page>')
  });
}
