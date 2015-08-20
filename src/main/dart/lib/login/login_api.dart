library login.api;

import 'dart:async';

abstract class LoginApi {
  
  Future<AuthRequestResult> login(String username, String password);
      
  Future<AuthRequestResult> logout();
  
  Future<StatusResult> getStatus();
  
  Future<ChangePasswordResult> changePassword(String oldPassword, String newPassword);
}

enum AuthRequestResult {
  OK,
  WRONG_CREDENTIALS,
  LOCKED_USER,
  NON_VALIDATED_USER
}

enum ChangePasswordResult {
  OK,
  WRONG_PASSWORD,
  INVALID_NEW_PASSWORD
}

enum StatusResult {
  OK,
  MUST_CHANGE_PASSWORD
}