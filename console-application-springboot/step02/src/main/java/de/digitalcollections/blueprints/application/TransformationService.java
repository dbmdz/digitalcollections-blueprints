package de.digitalcollections.blueprints.application;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

@Service
public class TransformationService {

  void transform(File xmlFile, File xslFile, StreamResult result) {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    try {
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document document = builder.parse(xmlFile);
      DOMSource xmlSource = new DOMSource(document);
      
      TransformerFactory tFactory = TransformerFactory.newInstance();
      StreamSource xslSource = new StreamSource(xslFile);
      Transformer transformer = tFactory.newTransformer(xslSource);
      
      transformer.transform(xmlSource, result);
    } catch (Exception ex) {
      // Parser with specified options can't be built
      ex.printStackTrace();
    }
  }
}
