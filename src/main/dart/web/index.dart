import 'dart:math';

import 'package:angular/angular.dart';
import 'package:angular/application_factory.dart';

import 'package:lci_generator/api/api.dart';
import 'package:lci_generator/api/api_impl.dart';
import 'package:lci_generator/custom_annotations.dart';

import 'package:lci_generator/process_generation_steps.dart';

void main() {
  applicationFactory()
      .addModule(new MyAppModule())
      .run();
}

class MyAppModule extends Module {
  MyAppModule() {
    bind(Api, toImplementation: ApiImpl);
    bind(ProcessGeneratorSteps);
    bind(String, toValue: new Random().nextInt(1<<31).toString(), withAnnotation: const IdTab() );
  }
}