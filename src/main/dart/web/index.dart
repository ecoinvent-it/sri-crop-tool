import 'dart:html';

import 'package:alcig/api/api.dart';
import 'package:alcig/api/api_impl.dart';
import 'package:alcig/api/generation_service.dart';
import 'package:alcig/api/local_notification_service.dart';
import 'package:alcig/connectivity_state.dart';
import 'package:alcig/license/license_api.dart';
import 'package:alcig/license/license_api_impl.dart';
import 'package:alcig/license/license_service.dart';
import 'package:alcig/login/login_api.dart';
import 'package:alcig/login/login_api_impl.dart';
import 'package:alcig/login/login_service.dart';
import 'package:alcig/toolMainPage/tool_main_page.dart';
import 'package:angular/angular.dart';
import 'package:angular_router/angular_router.dart';
import 'package:intl/date_symbol_data_local.dart';
import 'package:intl/intl.dart';
import 'package:intl/intl_browser.dart';
//import 'package:alcig/custom_annotations.dart';


void main() {
    _init().then((_) => window.postMessage('Dart is running', '*'));
}

_init() async {
    await findSystemLocale();
    await initializeDateFormatting(Intl.getCurrentLocale(), null);

    String serverUrl = Uri.base.port == 36123 ? "http://localhost:7879/" : "";

    ConnectivityState connectivityState = new ConnectivityState();

    var ref = await bootstrap(ToolMainPage, [ROUTER_BINDINGS, provide(LocationStrategy, useClass: HashLocationStrategy),
    provide(ConnectivityState, useValue: connectivityState),
    provide(Api, useValue: new ApiImpl(connectivityState, serverUrl)),
    provide(LicenseApi, useValue: new LicenseApiImpl(connectivityState, serverUrl)),
    provide(LoginApi, useValue: new LoginApiImpl(connectivityState, serverUrl)),
    provide(LicenseService),
    provide(LocalNotificationService),
    provide(GenerationService),
    provide(LoginService)
    ]);
}

