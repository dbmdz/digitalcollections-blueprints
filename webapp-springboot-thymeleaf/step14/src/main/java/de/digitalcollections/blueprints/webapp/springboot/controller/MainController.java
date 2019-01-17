package de.digitalcollections.blueprints.webapp.springboot.controller;

import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

  private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);

  @GetMapping(value = {"", "/"})
  public String printWelcome(Model model) {
    LOGGER.info( "Homepage requested");
    model.addAttribute("time", new Date());
    return "main";
  }
}
