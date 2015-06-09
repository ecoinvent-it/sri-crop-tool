import 'dart:html';
import 'dart:convert';
import 'package:angular/angular.dart';

import 'package:alcig/api/api.dart';
import 'package:alcig/custom_annotations.dart';

@Component(
    selector: 'process-generation-steps',
    templateUrl: 'packages/alcig/process_generation_steps.html',
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
  
  void submitUploadForm(target)
  {
    // FIXME: Find a better way
    upload(target.parent);
  }
  
  void upload(target)
  {
    // FIXME: Find a better way to avoid reading the file client side
    var formData = new FormData(target);
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