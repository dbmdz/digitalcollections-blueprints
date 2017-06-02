package de.digitalcollections.blueprints.crud.frontend.webapp.controller;

import java.sql.Timestamp;
import java.util.Date;
import de.digitalcollections.blueprints.crud.frontend.webapp.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.exceptions.TemplateInputException;

/**
 * Global exception handling.
 */
@ControllerAdvice
public class GlobalExceptionController implements EnvironmentAware {

  private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionController.class);
  private String activeProfile;

  @ExceptionHandler(ResourceNotFoundException.class)
  public ModelAndView handleResourceNotFoundException(Exception ex) {
    ModelAndView model = new ModelAndView("errors/error");
    model.addObject("timestamp", new Timestamp(new Date().getTime()));
    model.addObject("errorCode", "404");
    return model;
  }

  @Override
  public void setEnvironment(Environment environment) {
    String[] activeProfiles = environment.getActiveProfiles();
    if (activeProfiles.length == 1) {
      activeProfile = activeProfiles[0];
    }
  }

  @ExceptionHandler(value = {Exception.class, TemplateInputException.class})
  public ModelAndView handleAllException(Exception ex) {
    LOGGER.error("Internal Error", ex);
    ModelAndView model = new ModelAndView("errors/error");
    model.addObject("timestamp", new Timestamp(new Date().getTime()));
    model.addObject("errorCode", "500");
    if (!"PROD".equals(activeProfile)) {
      model.addObject("exception", ex);
    }
    return model;
  }
}
