library api.impl;

import 'dart:async';
import 'dart:convert';
import 'dart:html';

import 'api.dart';
import 'package:di/annotations.dart';
import 'package:alcig/connectivity_state.dart';

@Injectable()
class ApiImpl implements Api {
  static const String _baseUrl = "app/";
  static final String _baseApiUrl = _baseUrl + "api/";
  static final String _basePubApiUrl = _baseApiUrl + "pub/";
  
  ConnectivityState _connectivityState;
  
  // NOTE: use for notifications, check if it is still useful or must be put elsewhere
  StreamController<ServerEvent> _dispatcher = new StreamController.broadcast();
  Stream<ServerEvent> get stream => _dispatcher.stream;
  
  ApiImpl(ConnectivityState this._connectivityState);
  
  Future<List> getUserGenerations() async 
  {
    try
    {
      String result = await HttpRequest.getString(_baseApiUrl + "userGenerations");
      return JSON.decode(result);
    }
    catch(e)
    {
      _manageError(e);
    }
  }
  
  Future<HttpRequest> uploadInputs(dynamic formData){
      return HttpRequest.request(_baseApiUrl + "computeLci", method: "POST", sendData: formData)
                  .then((request) {return request;})
                  .catchError( (ProgressEvent e) {
                    HttpRequest request = e.target;
                    if ( request.status == 400 && request.responseText.isNotEmpty)
                      return request;
                    else
                      _manageError(e);
                  }
          );
    }
  
  Future<HttpRequest> contactUs(dynamic formData)
  {
    return HttpRequest.request(_basePubApiUrl + "contactUs", method: "POST", sendData: formData)
                      .then((request) {return request;})
                      .catchError( _manageError );
  }
  
  void _manageError(ProgressEvent e)
  {
    //_dispatcher.add(ServerEvent.SERVER_ERROR);
    _connectivityState.loggedOut();
    HttpRequest request = e.target;
    throw request.status;
  }

}
