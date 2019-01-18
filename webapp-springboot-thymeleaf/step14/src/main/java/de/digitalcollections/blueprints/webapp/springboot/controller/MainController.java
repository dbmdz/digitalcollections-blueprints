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

  private static int getLength(int i) {
    String s = null;

    if (i == 1) {
      s = "1";
    } else if (i == 2) {
      s = "2";
    }
    return s.length();
  }

  @GetMapping(value = {"", "/"})
  public String printWelcome(Model model) {
    LOGGER.info("Homepage requested");
    model.addAttribute("time", new Date());
    model.addAttribute("s", getLength(5));
    return "main";
  }
}
