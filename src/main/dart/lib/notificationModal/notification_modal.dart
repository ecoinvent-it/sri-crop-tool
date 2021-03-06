library alcig.notification_modal;

import 'dart:async';
import 'dart:js' as js;

import 'package:alcig/api/api.dart';
import 'package:alcig/api/local_notification_service.dart';
import 'package:angular/angular.dart';

@Component(
        selector: 'notification-modal',
        templateUrl: 'notification_modal.html')
class NotificationModal
        implements OnInit, OnDestroy
{
    Api _api;
    LocalNotificationService _notifService;

    NotificationModal(Api this._api, LocalNotificationService this._notifService);

    StreamSubscription _serverEventSubscription;
    StreamSubscription _localEventSubscription;

    String displayedText;
    bool isError = false;
    bool isWarning = false;

    @override
    void ngOnInit() {
        _serverEventSubscription = _api.stream.listen(_onServerEvent);
        _localEventSubscription = _notifService.stream.listen(_onLocalEvent);
    }

    @override
    void ngOnDestroy() {
        _serverEventSubscription.cancel();
        _serverEventSubscription = null;
        _localEventSubscription.cancel();
        _localEventSubscription = null;
    }

    void _onServerEvent(ServerEvent event) {
        isWarning = false;
        isError = true;
        switch (event)
        {
            default:
                displayedText =
                "Some connectivity issues occured. Please try again later. If it happens again, please contact us.";
        }
        _displayModal();
    }

    void _onLocalEvent(LocalEvent event) {
        isWarning = event.type == LocalEventType.Warning;
        isError = event.type == LocalEventType.Error;
        switch (event)
        {
            default:
                displayedText = event.message;
        }
        _displayModal();
    }

    void _displayModal()
    {
        js.context.callMethod(r'$', ['#notificationModal'])
                .callMethod('modal', [new js.JsObject.jsify({'show': 'true'})]);
    }
}