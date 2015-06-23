import 'dart:html';
import 'dart:convert';
import 'dart:js' as js;
import 'package:angular/angular.dart';

import 'package:alcig/api/api.dart';

@Component(
    selector: 'process-generation-steps',
    templateUrl: 'packages/alcig/process_generation_steps.html',
    useShadowDom: false)
class ProcessGeneratorSteps
{
  Api _api;
  
  // FIXME: Use enum when dart 1.9
  int step = 0;
  
  String filename = null;
  
  List warnings;
  List errors;
  String idResult;
  
  bool get hasWarnings => (warnings != null && warnings.length > 0);
  bool get hasErrors => (errors != null && errors.length > 0);
  
  ProcessGeneratorSteps(Api this._api);
  
  void submitUploadForm(target)
  {
    _reset();
    filename = (target as FileUploadInputElement).files[0].name;
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
      js.context.callMethod(r'$', ['#process-generation-step3-modal'])
              .callMethod('modal', [new js.JsObject.jsify({'show': 'true'})]);
    });
  }
  
  void _reset()
  {
    step = 0;
    idResult = null;
    warnings = null;
    errors = null;
  }

}