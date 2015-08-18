library login.api;

import 'dart:async';

abstract class LoginApi {
  
  Future<AuthRequestResult> login(String username, String password);
      
  Future<AuthRequestResult> logout();
}

enum AuthRequestResult {
  OK,
  OK_BUT_CHANGE_PASSWORD,
  WRONG_CREDENTIALS,
  LOCKED_USER,
  NON_VALIDATED_USER,
  WAS_LOGGED
}