package de.digitalcollections.blueprints.application.springboot.service;

import java.io.File;
import javax.xml.transform.Result;

public interface TransformationService {

  void transform(File xmlFile, File xslFile, Result result);
}
