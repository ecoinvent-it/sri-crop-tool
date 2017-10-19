library local_notification_service;

import 'dart:async';

import 'package:angular/di.dart';

@Injectable()
class LocalNotificationService
{
    StreamController<LocalEvent> _dispatcher = new StreamController.broadcast();

    Stream<LocalEvent> get stream => _dispatcher.stream;

    LocalNotificationService();

    manageWarning(String message) {
        _dispatcher.add(new LocalEvent(LocalEventType.Warning, message));
    }

    manageError(String message) {
        _dispatcher.add(new LocalEvent(LocalEventType.Warning, message));
    }

}

class LocalEvent
{
    LocalEventType type;
    String message;

    LocalEvent(LocalEventType this.type, String this.message);
}

enum LocalEventType
{
    Warning,
    Error
}