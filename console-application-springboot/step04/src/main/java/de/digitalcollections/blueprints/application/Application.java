package de.digitalcollections.blueprints.application;

import de.digitalcollections.blueprints.application.springboot.service.TransformationService;
import java.io.File;
import java.util.List;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import picocli.CommandLine;
import picocli.CommandLine.Option;

/**
 * Implementing ApplicationRunner interface tells Spring Boot to automatically
 * call the run method AFTER the application context has been loaded.
 */
@SpringBootApplication
public class Application implements ApplicationRunner {

  private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

  @Autowired
  private TransformationService transformationService;

  @Option(names = "--spring.profiles.active", description = "activate profile. Must be one of: default (active by default), info, debug")
  String profile;

  @Option(names = {"-h", "--help"}, usageHelp = true, description = "display a help message")
  private boolean helpRequested = false;

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
    CommandLine.populateCommand(Application.class, args);
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {
    LOGGER.info("STARTING THE APPLICATION");

    if (args.getSourceArgs().length < 2) {
      System.err.println("Need two arguments: filepath of XML and filepath of XSL.");
      System.exit(1);
    }

    final List<String> filepaths = args.getNonOptionArgs();
    String filepathXml = filepaths.get(0);
    String filepathXsl = filepaths.get(1);

    File xmlFile = new File(filepathXml);
    File xslFile = new File(filepathXsl);

    Result result = new StreamResult(System.out);
    transformationService.transform(xmlFile, xslFile, result);
  }

}
