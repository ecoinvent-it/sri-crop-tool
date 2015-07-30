import 'package:angular/angular.dart';

import 'dart:html';

@Decorator(selector: '[menuWithSelection]')
class MenuWithSelection {
  
  final Element element;
  
  String selectedPage;
  
  Router _router;
  
  MenuWithSelection(Element this.element, Router this._router) {
    _router.onRouteStart.listen((RouteStartEvent event) {
          event.completed.then((_) {
            if (_router.activePath.length > 0) {
              selectedPage = _router.activePath[0].name;
            } else {
              selectedPage = null;
            }
            _manageMenuItemStyle();
          });
        }); 
  }
  
  void _manageMenuItemStyle()
  {
    element.querySelectorAll(".menuItem").classes.remove("selectedmenuItem");
    String currentSelectedItemMenu;
    switch(selectedPage)
    {
      case 'contactUs':
        currentSelectedItemMenu = "#contactUsMenuItem";
        break;
        
      case 'tool':
      default:
        currentSelectedItemMenu = "#homeMenuItem";
        break;
    }
    element.querySelector(currentSelectedItemMenu).classes.add("selectedmenuItem");
  }
}