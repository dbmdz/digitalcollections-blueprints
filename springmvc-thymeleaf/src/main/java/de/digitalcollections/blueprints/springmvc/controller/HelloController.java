package de.digitalcollections.blueprints.springmvc.controller;

import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = {"", "/"})
public class HelloController {

  private static final Logger LOGGER = LoggerFactory.getLogger(HelloController.class);

  @RequestMapping(method = RequestMethod.GET)
  public String printWelcome(Model model) {
    LOGGER.info("Homepage requested");
    model.addAttribute("time", new Date());
    return "main";
  }
}
