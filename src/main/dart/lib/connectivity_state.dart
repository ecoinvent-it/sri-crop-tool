library connectivity_state;

import 'dart:async';
import 'package:di/annotations.dart';

@Injectable()
class ConnectivityState {
  
  StreamController<StateEvent> _dispatcher = new StreamController.broadcast();
  
  bool _isOnline = true;
  bool _isLogged = false;
  
  Timer _autoLogoutAfterDisconnectivity;
  
  Stream<StateEvent> get stream => _dispatcher.stream;
  
  bool get isOnline => _isOnline;
  bool get isLogged => _isLogged;
  
  ConnectivityState();
  
  void putOffline() 
  {
    if ( isOnline ) 
    {
      _isOnline = false;
      _autoLogoutAfterDisconnectivity = new Timer(new Duration(minutes: 15), () => loggedOut());
      _dispatcher.add(StateEvent.OFFLINE);
    }
  }
  
  void restoreOnline() 
  {
    if ( !isOnline ) 
    {
      _isOnline = true;
      _autoLogoutAfterDisconnectivity.cancel();
      _autoLogoutAfterDisconnectivity = null;
      _dispatcher.add(StateEvent.ONLINE);
    }
  }
  
  void loggedIn() 
  {
    if ( !isLogged ) 
    {
      _isLogged = true;
      _dispatcher.add(StateEvent.AUTHED);
    }
  }
  
  void loggedOut() 
  {
    if ( isLogged ) 
    {
      _isLogged = false;
      _dispatcher.add(StateEvent.NOT_AUTHED);
    }
  }
}

enum StateEvent {
  ONLINE,
  OFFLINE,
  AUTHED,
  NOT_AUTHED
}