library api;

import 'dart:async';
import 'dart:html';

abstract class Api {
  
  Future<HttpRequest> uploadInputs(dynamic data);
}