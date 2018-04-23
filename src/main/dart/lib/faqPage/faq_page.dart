library faq;

import 'package:angular/angular.dart';

@Component(
        selector: 'faq-page',
        styleUrls: const ['faq_page.css'],
        templateUrl: 'faq_page.html')
class FaqPage
{
  FaqPage();

  String ecoinventEmail = 'LCI_tool@ecoinvent.org';
}