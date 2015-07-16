library login.service;

import 'dart:async';
import 'package:di/annotations.dart';
import 'package:alcig/login/login_api.dart';

@Injectable()
class LoginService {
  
  LoginApi _loginApi;
  
  StreamController<LoginEvent> _dispatcher = new StreamController.broadcast();
  Stream<LoginEvent> get stream => _dispatcher.stream;
  
  String _username;
    
  bool _isLoading = false;

  String get username => _username;
      
  bool get isLogged => _username != null;
  
  bool get isLoading => _isLoading;
  
  LoginService(LoginApi this._loginApi);
  
  void login(String username, String password) {
    if ( !isLoading ) {
      _isLoading = true;
      _dispatcher.add(LoginEvent.AUTHENTICATING);
      
      _loginApi.login(username, password)
               .then( (RequestResult status) {
                   switch ( status ) {
                     case RequestResult.OK:
                       _dispatcher.add(LoginEvent.AUTHENTICATED);
                       _username = username;
                       break;
                     case RequestResult.WRONG_CREDENTIALS:
                       _dispatcher.add(LoginEvent.WRONG_CREDENTIALS);
                       break;
                     case RequestResult.LOCKED_USER:
                       _dispatcher.add(LoginEvent.LOCKED_USER);
                       break;
                     case RequestResult.NON_VALIDATED_USER:
                       _dispatcher.add(LoginEvent.NON_VALIDATED_USER);
                       break;
                     default:
                       throw status;
                   }
               })
               .catchError( (e) {
                        print("Server error: $e");
                        _dispatcher.add(LoginEvent.SERVER_ERROR);
                      })
               .whenComplete(() => _isLoading = false);
    }
  }
  
  void logout() {
    if ( ! isLoading ) {
      _isLoading = true;
      _dispatcher.add(LoginEvent.LOGGING_OUT);

      _loginApi.logout()
               .then((RequestResult rs) => _dispatcher.add(LoginEvent.LOGGED_OUT))
               .catchError( (e) {
                 print("Server error: $e");
                 _dispatcher.add(LoginEvent.LOG_OUT_UNSURE);
               })
               .whenComplete(() {
                 _isLoading = false;
                 _username = null;
               });
    }
  }
}

enum LoginEvent {
  AUTHENTICATING,
  AUTHENTICATED,
  WRONG_CREDENTIALS,
  LOCKED_USER,
  NON_VALIDATED_USER,
  SERVER_ERROR,
  LOGGING_OUT,
  LOGGED_OUT,
  LOG_OUT_UNSURE,
  LOG_OUT_BY_SERVER
}