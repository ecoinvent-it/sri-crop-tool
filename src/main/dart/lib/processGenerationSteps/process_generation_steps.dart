library process_generation_steps;

import 'dart:async';
import 'dart:html';
import 'dart:convert';
import 'dart:js' as js;
import 'package:angular/angular.dart';

import 'package:alcig/api/api.dart';
import 'package:alcig/api/local_notification_service.dart';
import 'package:alcig/login/login_service.dart';

@Component(
    selector: 'process-generation-steps',
    templateUrl: 'packages/alcig/processGenerationSteps/process_generation_steps.html',
    useShadowDom: false)
class ProcessGeneratorSteps
{
  Api _api;
  LocalNotificationService _notifService;
  LoginService _loginService;
  
  // FIXME: Use enum when angular dart 2? (not ok for binding with 1.9)
  int step = 0;
  
  String filename = null;
  FormElement step3Form;
  
  List warnings;
  List errors;
  String idResult;
  
  bool fileCanBeStored = true;
  
  bool get hasWarnings => (warnings != null && warnings.length > 0);
  bool get hasErrors => (errors != null && errors.length > 0);
  
  bool get isLogged => _loginService.isLogged;

  static const FILE_MAX_SIZE = 10 * 1024 * 1024;// 10 Mo
  
  ProcessGeneratorSteps(Api this._api, LoginService this._loginService, LocalNotificationService this._notifService);
  
  void disabledStep3Click()
  {// TODO: Have a more user friendly message
    if (step == 0 && !_loginService.isLogged)
      _notifService.manageWarning("You need to be authenticated to use this feature");
  }
  
  void submitUploadForm(target)
  {
    // TODO: dirty
    step3Form = target.parent.parent as FormElement;
    File file = (target as FileUploadInputElement).files[0];
    if (file.size > FILE_MAX_SIZE)
    {
      _notifService.manageError("Oh that's a too big file! Maximum authorized is 10Mo" );
      reset();
    }
    else
    {
      filename = file.name;
      _upload();
    }
    
  }
  
  void _upload()
  {
    var formData = new FormData(step3Form);
    formData.append("filename", filename);
    // TODO: remove these args
    formData.append("username", "");
    formData.append("email", "");
    formData.append("address", "");
    displayModal("#fileLoadingModal");
    _api.uploadInputs(formData)
    .then((HttpRequest request) {
      
      if ( request.status == 400 )
      {
        reset();
        Map map = JSON.decode(request.responseText); 
        warnings = map['warnings']..sort(_errorsOrWarningsComparator);
        errors = map['errors']..sort(_errorsOrWarningsComparator);
      }
      else
      {
        step = 1;
        Map map = JSON.decode(request.responseText); 
        warnings = map['message']['warnings']..sort(_errorsOrWarningsComparator);
        errors = map['message']['errors']..sort(_errorsOrWarningsComparator);
        idResult = map['idResult'];
      }
      hideModal("#fileLoadingModal");
      displayModal('#process-generation-step3-modal');
    })
    .catchError((_)
      { 
        reset(); hideModal("#fileLoadingModal");
      });
  }

  void hideModal(String id)
  {
    js.context.callMethod(r'$', [id])
                  .callMethod('modal', ['hide']);
  }
  
  void displayModal(String id)
  {
    js.context.callMethod(r'$', [id])
                  .callMethod('modal', [new js.JsObject.jsify({'show': 'true'})]);
  }
  
  void reset()
  {
    step = 0;
    idResult = null;
    warnings = null;
    errors = null;
    // NOTE: Needed to be able to send again the same file
    step3Form.reset();
  }
  
  int _errorsOrWarningsComparator(a,b)
  {
    String cellA = a['context']['cell'];
    String cellB = b['context']['cell'];
    if (cellA == null || cellA == "")
      return -1;
    if (cellB == null || cellB == "")
      return 1;
    
    int lineComparator = cellA.substring(1,cellA.length)
        .compareTo(cellB.substring(1,cellB.length));
    
    if (lineComparator == 0)
      return cellA.compareTo(cellB);
    else
      return lineComparator;
  }
  
  bool isChecked = false;

  Future checkScsvGeneration(Event e) async
  {
    if (!isChecked)
    {
      e.preventDefault();
      FormElement form = e.target;
      String dbOption = (form.querySelector('input[name=dbOption]:checked') as RadioButtonInputElement).value;
      Map data = new Map();
      data["dbOption"] = dbOption;
      data["idResult"] = idResult;
      data["filename"] = filename;
      await _api.checkScsvGeneration(data);
      isChecked = true;
      form.submit();
    }
    else
      isChecked = false;
  }

}
