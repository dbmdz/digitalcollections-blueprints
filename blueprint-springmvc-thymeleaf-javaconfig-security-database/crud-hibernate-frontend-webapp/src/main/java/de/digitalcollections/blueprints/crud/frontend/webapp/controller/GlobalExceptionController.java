package de.digitalcollections.blueprints.crud.frontend.webapp.controller;

import de.digitalcollections.blueprints.crud.frontend.webapp.exceptions.ResourceNotFoundException;
import java.sql.Timestamp;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.thymeleaf.exceptions.TemplateInputException;

/**
 * Global exception handling.
 */
@ControllerAdvice
public class GlobalExceptionController implements EnvironmentAware {

  private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionController.class);
  private String activeProfile;

  @ExceptionHandler(ResourceNotFoundException.class)
  public String handleResourceNotFoundException(Exception ex, Model model) {
    model.addAttribute("timestamp", new Timestamp(new Date().getTime()));
    model.addAttribute("errorCode", "404");
    return "errors/error";
  }

  @Override
  public void setEnvironment(Environment environment) {
    String[] activeProfiles = environment.getActiveProfiles();
    if (activeProfiles.length == 1) {
      activeProfile = activeProfiles[0];
    }
  }

  @ExceptionHandler(value = {Exception.class, TemplateInputException.class})
  public String handleAllException(Exception ex, Model model) {
    LOGGER.error("Internal Error", ex);
    model.addAttribute("timestamp", new Timestamp(new Date().getTime()));
    model.addAttribute("errorCode", "500");
    if (!"PROD".equals(activeProfile)) {
      model.addAttribute("exception", ex);
    }
    return "errors/error";
  }
}
