library faq;

import 'package:angular/angular.dart';

@Component(
    selector: 'faq-page',
    templateUrl: 'packages/alcig/faqPage/faq_page.html',
    useShadowDom: false)
class FaqPage
{
  FaqPage();
  
  String quantisEmail = 'software-support@quantis-intl.com';
}