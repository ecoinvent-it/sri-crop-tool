library license.api;

import 'dart:async';

abstract class LicenseApi {
  
  Future<List> getUserLicenses();
}