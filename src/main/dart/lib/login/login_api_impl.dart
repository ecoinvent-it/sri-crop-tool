library login.api.impl;

import 'dart:async';
import 'dart:html';

import 'login_api.dart';
import 'package:di/annotations.dart';

@Injectable()
class LoginApiImpl implements LoginApi {
  static const String _baseUrl = "app/";

  LoginApiImpl();
  
  Future<RequestResult> login(String username, String password) {
        Map<String, String> params = new Map();
        params["username"] = username;
        params["password"] = password;
          
        return HttpRequest.postFormData(_baseUrl + "login", params)
            .then((_) { return RequestResult.OK; })
            .catchError( (ProgressEvent e) {
                  HttpRequest request = e.target;
                  if ( request.status == 401 ) {
                    switch ( request.responseText ) {
                      case 'WRONG_CREDENTIALS':
                        return RequestResult.WRONG_CREDENTIALS;
                      case 'LOCKED_USER':
                        return RequestResult.LOCKED_USER;
                      case 'NON_VALIDATED_USER':
                        return RequestResult.NON_VALIDATED_USER;
                      default:
                        throw request;
                    }
                  }
                  else //TODO: Test what happens in case of timeout
                    throw request;
                }
            );
      }
      
      //TODO: In case of failure, we should retry
      Future<RequestResult> logout() {
        return HttpRequest.postFormData(_baseUrl + "logout", {})
            .then((_) { return RequestResult.OK; })
            .catchError( (ProgressEvent e) {
                  HttpRequest request = e.target;
                  if ( request.status == 401 ) {
                    return RequestResult.OK;
                  }
                  else
                    throw request;
                }
        );
      }
}