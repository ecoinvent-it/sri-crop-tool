library login.api;

import 'dart:async';

abstract class LoginApi {
  
  Future<AuthRequestResult> login(String username, String password);
      
  Future<AuthRequestResult> logout();
  
  Future<StatusResult> getStatus();
  
  Future<ChangePasswordResult> changePassword(String oldPassword, String newPassword);
  
  Future<ForgotPasswordResult> forgotPassword(String email);
  
  Future<ResetPasswordResult> resetPassword(String email, String validationCode, String newPassword);
}

enum AuthRequestResult {
  OK,
  WRONG_CREDENTIALS,
  LOCKED_USER,
  NON_ACTIVATED_USER
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

enum ForgotPasswordResult {
  OK,
  USER_ACTIVATION_PENDING
}

enum ResetPasswordResult {
  OK,
  WRONG_VALIDATION_CODE,
  EXPIRED_VALIDATION_CODE,
  INVALID_NEW_PASSWORD
}