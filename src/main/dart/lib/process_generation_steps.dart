import 'dart:html';
import 'dart:convert';
import 'dart:js' as js;
import 'package:angular/angular.dart';

import 'package:alcig/api/api.dart';
import 'package:alcig/api/local_notification_service.dart';
import 'package:alcig/api/user.dart';

@Component(
    selector: 'process-generation-steps',
    templateUrl: 'packages/alcig/process_generation_steps.html',
    useShadowDom: false)
class ProcessGeneratorSteps
{
  Api _api;
  LocalNotificationService _notifService;
  User _user;
  
  String get userEmail => _user.email;
  String get userName => _user.name;
  String get userAddress => _user.address;
  bool get isEmailValid => _user.isEmailValid;
  
  // FIXME: Use enum when dart 2? (not ok for binding with 1.9)
  int step = 0;
  
  String filename = null;
  FormElement step3Form;
  
  List warnings;
  List errors;
  String idResult;
  
  bool fileCanBeStored = true;
  
  bool get hasWarnings => (warnings != null && warnings.length > 0);
  bool get hasErrors => (errors != null && errors.length > 0);
  
  ProcessGeneratorSteps(Api this._api, User this._user, LocalNotificationService this._notifService);
  
  void disabledStep3Click()
  {// TODO: Have a more user friendly message
    if (step == 0 && !isEmailValid)
      _notifService.manageWarning("You need to enter your email address before using this feature");
  }
  
  void submitUploadForm(target)
  {
    filename = (target as FileUploadInputElement).files[0].name;
    // FIXME: Find a better way
    step3Form = target.parent as FormElement;
    _upload();
  }
  
  void _upload()
  {
    // FIXME: Find a better way to avoid reading the file client side
    var formData = new FormData(step3Form);
    _api.uploadInputs(formData)
    .then((HttpRequest request) {
      if ( request.status == 400 )
      {
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
      
      js.context.callMethod(r'$', ['#process-generation-step3-modal'])
              .callMethod('modal', [new js.JsObject.jsify({'show': 'true'})]);
    })
    .catchError((_) => reset());
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

}