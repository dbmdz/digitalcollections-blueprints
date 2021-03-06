package de.digitalcollections.blueprints.rest.server;

import org.jsondoc.spring.boot.starter.EnableJSONDoc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableJSONDoc
@SpringBootApplication
/*
 @SpringBootApplication is a convenience annotation that adds all of the following:
 - @Configuration tags the class as a source of bean definitions for the application context.
 - @EnableAutoConfiguration tells Spring Boot to start adding beans based on classpath settings, other beans, and various property settings.
 - Normally you would add @EnableWebMvc for a Spring MVC app, but Spring Boot adds it automatically when it sees spring-webmvc on the classpath. This flags the application as a web application and activates key behaviors such as setting up a DispatcherServlet.
 - @ComponentScan tells Spring to look for other components, configurations, and services in the current package (<b>and subpackages</b>), allowing it to find controllers, too.
 */
public class Application {

  public static void main(String[] args) throws Exception {
    // if (System.getProperty("spring.profiles.active") == null) {
    //   System.setProperty("spring.profiles.active", "local");
    // }

    SpringApplication.run(Application.class, args);
  }
}
