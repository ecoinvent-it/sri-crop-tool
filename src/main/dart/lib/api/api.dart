library api;

import 'dart:async';
import 'dart:html';

abstract class Api {

  Stream<ServerEvent> get stream;

  String get serverUrl;

  Future<List> getUserGenerations();

  Future<HttpRequest> uploadInputs(dynamic data);

  Future<HttpRequest> contactUs(dynamic data);
}

class ServerEvent {
  static const ServerEvent SERVER_ERROR = const ServerEvent("SERVER_ERROR");

  final String name;

  const ServerEvent(String this.name);
}
