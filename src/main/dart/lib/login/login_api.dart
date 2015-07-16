library login.api;

import 'dart:async';

abstract class LoginApi {
  
  Future<RequestResult> login(String username, String password);
      
  Future<RequestResult> logout();
}

enum RequestResult {
  OK,
  WRONG_CREDENTIALS,
  LOCKED_USER,
  NON_VALIDATED_USER,
  WAS_LOGGED
}