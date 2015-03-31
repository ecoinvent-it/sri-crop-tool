import 'dart:html';
import 'dart:convert';
import 'package:angular/angular.dart';

import 'package:lci_generator/api/api.dart';
import 'package:lci_generator/custom_annotations.dart';

@Component(
    selector: 'process-generation-steps',
    templateUrl: 'packages/lci_generator/process_generation_steps.html',
    useShadowDom: false)
class ProcessGeneratorSteps
{
  Api _api;
  
  // FIXME: Use enum when dart 1.9
  int step = 0;
  
  List warnings;
  List errors;
  String idResult;
  
  String idTab;
  
  ProcessGeneratorSteps(Api this._api, @IdTab() String this.idTab);
  
  void upload()
  {
    // FIXME: Find a better way to avoid reading the file client side
    var formData = new FormData(querySelector("#processGeneratorUploadForm"));
    _api.uploadInputs(formData)
    .then((HttpRequest request) {
      if ( request.status == 400 )
      {
        Map map = JSON.decode(request.responseText); 
        warnings = map['warnings'];
        errors = map['errors'];
      }
      else
      {
        step = 1;
        Map map = JSON.decode(request.responseText); 
        warnings = map['message']['warnings'];
        errors = map['message']['errors'];
        idResult = map['idResult'];
      }
    });
  }
  
  void reset()
  {
    warnings = null;
    errors = null;
    idResult = null;
    step = 0;
  }

}