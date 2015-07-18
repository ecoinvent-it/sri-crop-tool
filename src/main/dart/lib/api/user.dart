library user;

class User
{
  String email = "";
  String name = "";
  String address = "";
  
// FIXME Find a better regExp or another email validation solution
  // Src: http://askstop.com/questions/2319753/validate-email-address-in-dart
  // checked with examples in https://en.wikipedia.org/wiki/Email_address#Valid_email_addresses
  // Valid email addresses that don't pass with this regExp:
  // admin@mailserver1 (local domain name with no TLD)
  // üñîçøðé@üñîçøðé.com (Unicode characters in domain part)
  static final String EMAIL_PATTERN = r'^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$';
  static final RegExp EMAIL_REGEXP = new RegExp(EMAIL_PATTERN);
  
  bool get isEmailValid => (email != "" && EMAIL_REGEXP.hasMatch(email));
  
  User();
}