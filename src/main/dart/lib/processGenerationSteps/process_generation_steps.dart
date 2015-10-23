library process_generation_steps;

import 'dart:async';
import 'dart:collection';
import 'dart:convert';
import 'dart:html';
import 'dart:js' as js;
import 'package:angular/angular.dart';

import 'package:alcig/api/api.dart';
import 'package:alcig/api/local_notification_service.dart';
import 'package:alcig/login/login_service.dart';

@Component(
    selector: 'process-generation-steps',
    templateUrl: 'packages/alcig/processGenerationSteps/process_generation_steps.html',
    useShadowDom: false)
class ProcessGeneratorSteps implements AttachAware, DetachAware
{
  Api _api;
  LocalNotificationService _notifService;
  LoginService _loginService;
  
  StreamSubscription _userSubscription;
  
  // FIXME: Put all these in a dedicated popup component
  String popupFilename = null;
  List popupWarnings;
  List popupErrors;
  bool get popupHasErrors => popupErrors != null && popupErrors.length > 0;
  bool get popupHasWarnings => popupWarnings != null && popupWarnings.length > 0;
  bool get popupHasOnlyWarnings => popupHasWarnings && !popupHasErrors;
  
  //FIXME: Put in a generation service
  Map lastGeneration;
  String get lastGenerationId => lastGeneration == null ? null : lastGeneration['id']['representation'];
  List generations = null;
  
  bool fileCanBeStored = true;
  
  bool hasWarnings(Map generation) => generation['warnings'] != null && generation['warnings'].length > 0;
  
  bool get isLogged => _loginService.isLogged;

  static const FILE_MAX_SIZE = 10 * 1024 * 1024;// 10 Mo
  
  ProcessGeneratorSteps(Api this._api, LoginService this._loginService, LocalNotificationService this._notifService);
  
  void attach() 
  {
    _userSubscription = _loginService.stream.listen(_onUserData);
  }
    
  void detach() 
  {
    _userSubscription.cancel();
    _userSubscription = null;
  }
  
  void _onUserData(LoginEvent event) 
  {
    switch ( event) 
    {
      case LoginEvent.AUTHENTICATED:
        _loadGenerations();
        break;
      case LoginEvent.LOGGED_OUT:
      case LoginEvent.LOG_OUT_UNSURE:
      case LoginEvent.LOG_OUT_BY_SERVER:
        _resetAll();
        break;
      default:
        break;
    }
  }
  
  void changeSelectedGeneration(Map generation)
  {
    lastGeneration = generation;
  }
  
  String displayDate(List dateItems)
  {
    String day = dateItems[0].toString() + "-" 
                + _get2DigitString(dateItems[1]) + "-" 
                + _get2DigitString(dateItems[2]);
    String time =  _get2DigitString(dateItems[3]) + ":" 
                + _get2DigitString(dateItems[4]);
    return day + " " + time;
  }
  
  String _get2DigitString(int value)
  {
    String stringValue = value.toString();
    if (stringValue.length == 1)
      stringValue = "0" + stringValue;
    return stringValue;
  }
  
  void disabledStep3Click()
  {// TODO: Have a more user friendly message
    if (!_loginService.isLogged)
      _notifService.manageWarning("You need to be authenticated to use this feature");
  }
  
  Future _loadGenerations() async
  {
    generations = await _api.getUserGenerations();
  }
  
  void submitUploadForm(FileUploadInputElement target, FormElement form)
  {
    File file = target.files[0];
    if (file.size > FILE_MAX_SIZE)
    {
      _notifService.manageError("Oh that's a too big file! Maximum authorized is 10Mo" );
      form.reset();
    }
    else
    {
      _upload(file.name, form);
    }
    
  }
  
  void submitReuploadForm(FileUploadInputElement target, FormElement form, Map generation)
  {
    changeSelectedGeneration(generation);
    submitUploadForm(target, form);
  }
  
  Future _upload(String filename, FormElement form) async
  {
    var formData = new FormData(form);
    formData.append("filename", filename);
    formData.append("generationId", lastGenerationId);
    
    displayModal("#fileLoadingModal");

    Map uploadedGeneration = lastGeneration;
    List warnings;
    List errors;
    try
    {
      form.reset();
      HttpRequest request = await _api.uploadInputs(formData);
    
      if ( request.status == 400 )
      {
        Map map = JSON.decode(request.responseText); 
        warnings = map['warnings']..sort(_errorsOrWarningsComparator);
        errors = map['errors']..sort(_errorsOrWarningsComparator);
      }
      else
      {
        Map newGeneration = JSON.decode(request.responseText);
        if (uploadedGeneration != null)
        {
          if (uploadedGeneration['id']['representation'] == newGeneration['id']['representation'])
            generations.remove(uploadedGeneration);
        }
        generations.insert(0, newGeneration);
        changeSelectedGeneration(newGeneration);
        warnings = newGeneration['warnings'];
        errors = [];
      }
    }
    finally
    { 
      hideModal("#fileLoadingModal");
    }
    displayStatusModal(filename, errors, warnings);
  }
  
  void changeGenerationAndDisplayUpdateStatus(Map generation)
  {
    changeSelectedGeneration(generation);
    displayStatusModal(generation['filename'], [], generation['warnings']);
  }
  
  void displayStatusModal(String filename, List errors, List warnings)
  {
    popupFilename = filename;
    popupErrors = errors;
    popupWarnings = warnings;
    displayModal('#process-generation-step3-modal');
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
  
  void _resetAll()
  {
    lastGeneration = null;
    generations = null;
    popupFilename = null;
    popupErrors = null;
    popupWarnings = null;
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
