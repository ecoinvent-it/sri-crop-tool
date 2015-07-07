library alcig.notification_modal;

import 'dart:async';
import 'dart:js' as js;
import 'package:angular/angular.dart';
import 'package:alcig/api/api.dart';

@Component(
    selector: 'notification-modal',
    templateUrl: 'packages/alcig/notificationModal/notification_modal.html',
    useShadowDom: false)
class NotificationModal implements AttachAware, DetachAware
{
  Api _api;
  
  NotificationModal(Api this._api);
  
  StreamSubscription _serverEventSubscription;
  
  String displayedText;
  
  void attach() {
    _serverEventSubscription = _api.stream.listen(_onServerEvent);
  }
    
  void detach() {
    _serverEventSubscription.cancel();
    _serverEventSubscription = null;
  }
  
  void _onServerEvent(ServerEvent event) {
    switch(event)
    {
      default:
        displayedText = "Some connectivity issues occured. Please try again later. If it happens again, please contact us.";
    }
    js.context.callMethod(r'$', ['#notificationModal'])
                  .callMethod('modal', [new js.JsObject.jsify({'show': 'true'})]);
  }
}