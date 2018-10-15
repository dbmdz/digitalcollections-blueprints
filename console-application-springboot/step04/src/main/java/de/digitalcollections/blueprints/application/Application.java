package de.digitalcollections.blueprints.application;

import de.digitalcollections.blueprints.application.cli.Cli;
import de.digitalcollections.blueprints.application.cli.CliException;
import de.digitalcollections.blueprints.application.cli.ExitStatus;
import de.digitalcollections.blueprints.application.springboot.service.TransformationService;
import java.io.File;
import java.io.PrintWriter;
import java.util.List;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Implementing ApplicationRunner interface tells Spring Boot to automatically
 * call the run method AFTER the application context has been loaded.
 */
@SpringBootApplication
public class Application implements ApplicationRunner {

  private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

  @Autowired
  private TransformationService transformationService;

  public static void main(String[] args) {
    processArguments(args);
    SpringApplication.run(Application.class, args);
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

  private static void processArguments(String... args) {
    Cli cli;
    try {
      cli = new Cli(new PrintWriter(System.out), args);
      if (cli.hasExitStatus()) {
        System.exit(cli.getExitStatus());
      }
      if (cli.hasSpringProfiles()) {
        System.setProperty("spring.profiles.active", cli.getSpringProfiles());
      }
    } catch (CliException e) {
      LOGGER.error(e.getMessage());
      System.exit(ExitStatus.ERROR);
    } catch (ParseException e) {
      LOGGER.error("Could not parse command line arguments", e);
      System.exit(ExitStatus.ERROR);
    }
  }
}
