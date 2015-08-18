library api.impl;

import 'dart:async';
import 'dart:html';

import 'api.dart';
import 'package:di/annotations.dart';

@Injectable()
class ApiImpl implements Api {
  static const String _baseUrl = "app/";
  static final String _baseApiUrl = _baseUrl + "api/";
  static final String _basePubApiUrl = _baseApiUrl + "pub/";

  ApiImpl();
  
  StreamController<ServerEvent> _dispatcher = new StreamController.broadcast();
  Stream<ServerEvent> get stream => _dispatcher.stream;
  
  Future<HttpRequest> uploadInputs(dynamic formData){
      return HttpRequest.request(_baseApiUrl + "computeLci", method: "POST", sendData: formData)
                  .then((request) {return request;})
                  .catchError( (ProgressEvent e) {
                    HttpRequest request = e.target;
                    if ( request.status == 400 )
                      return request;
                    else
                      _manageError(e);
                  }
          );
    }
  
  Future<HttpRequest> checkScsvGeneration(Map data)
  {
    return HttpRequest.postFormData(_baseApiUrl + "checkScsvGeneration", data)
                      .then((request) {return request;})
                      .catchError( _manageError );
  }
  
  Future<HttpRequest> contactUs(dynamic formData)
  {
    return HttpRequest.request(_basePubApiUrl + "contactUs", method: "POST", sendData: formData)
                      .then((request) {return request;})
                      .catchError( _manageError );
  }
  
  void _manageError(ProgressEvent e)
  {
    _dispatcher.add(ServerEvent.SERVER_ERROR);
    throw e;
  }

}
