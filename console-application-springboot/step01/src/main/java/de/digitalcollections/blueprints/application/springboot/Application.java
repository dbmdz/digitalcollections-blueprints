package de.digitalcollections.blueprints.application.springboot;

import de.digitalcollections.blueprints.application.springboot.service.HelloMessageService;
import java.util.List;
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
  private HelloMessageService helloService;

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
    
//    SpringApplication app = new SpringApplication(Application.class);
//    app.setBannerMode(Banner.Mode.OFF);
//    app.setLogStartupInfo(false);
//    app.run(args);
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {
    LOGGER.info("STARTING THE APPLICATION");
    
    final List<String> names = args.getNonOptionArgs();
    String message = helloService.getMessage(names);
    System.out.println(message);
  }

}
