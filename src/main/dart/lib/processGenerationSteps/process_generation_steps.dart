library process_generation_steps;

import 'dart:async';
import 'dart:html';
import 'dart:js' as js;
import 'package:angular/angular.dart';

import 'package:alcig/api/generation_service.dart';
import 'package:alcig/api/local_notification_service.dart';
import 'package:alcig/license/license_service.dart';
import 'package:alcig/login/login_service.dart';
import 'package:alcig/date_utils.dart';

@Component(
    selector: 'process-generation-steps',
    templateUrl: 'packages/alcig/processGenerationSteps/process_generation_steps.html',
    useShadowDom: false)
class ProcessGeneratorSteps
{
  LocalNotificationService _notifService;
  LoginService _loginService;
  GenerationService _generationService;
  LicenseService _licenseService;

  // FIXME: Put all these in a dedicated popup component
  String popupFilename = null;
  List popupWarnings;
  List popupErrors;
  bool get popupHasErrors => popupErrors != null && popupErrors.length > 0;
  bool get popupHasWarnings => popupWarnings != null && popupWarnings.length > 0;
  bool get popupHasOnlyWarnings => popupHasWarnings && !popupHasErrors;
  
  Map get lastGeneration => _generationService.lastGeneration;
  String get lastGenerationId => _generationService.lastGenerationId;
  List get generations => _generationService.generations;
  
  Map get currentLicense => _licenseService.currentLicense;
  
  bool fileCanBeStored = true;
  
  bool hasWarnings(Map generation) => generation['warnings'] != null && generation['warnings'].length > 0;
  
  bool get isLogged => _loginService.isLogged;

  static const FILE_MAX_SIZE = 10 * 1024 * 1024;// 10 Mo
  
  ProcessGeneratorSteps(GenerationService this._generationService, LoginService this._loginService, 
                        LicenseService this._licenseService, LocalNotificationService this._notifService);
  
  void changeSelectedGeneration(Map generation)
  {
    _generationService.changeSelectedGeneration(generation);
  }
  
  DateTime _parseDateTime(String dateTime) => DateUtils.parseDateTime(dateTime);
  String displayDateTime(String dateTime) => DateUtils.displayDateTime(dateTime);
  String displayDate(String date) => DateUtils.displayDate(date);
  
  String displayRentalItem(Map license) => _licenseService.displayRentalItem(license);
  
  int getNbRemainingGenerationsForLicense(List generations, Map license) => 
            _generationService.getNbRemainingGenerationsForLicense(generations,license);
  
  bool canRetry(Map generation) => _parseDateTime(generation['lastTryDate']).add(new Duration(minutes:30)).isAfter(new DateTime.now());
  
  void disabledStep3Click()
  {// TODO: Have a more user friendly message
    if (!_loginService.isLogged)
      _notifService.manageWarning("You need to be authenticated to use this feature.");
    else if (currentLicense['isDepleted'])
      _notifService.manageWarning("Your license is depleted. Please contact your reseller to get a new license.");
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
    form.reset();
    
    List warnings;
    List errors;
    try
    {
      Map results = await _generationService.upload(filename, formData, currentLicense);
      warnings = results['warnings'];
      if (results['errors'] != null)
        errors = results['errors'];
      else
        errors = [];
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
    popupErrors = errors..sort(_errorsOrWarningsComparator);
    popupWarnings = warnings..sort(_errorsOrWarningsComparator);
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
