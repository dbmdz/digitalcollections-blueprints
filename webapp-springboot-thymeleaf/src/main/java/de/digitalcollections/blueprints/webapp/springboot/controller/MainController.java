package de.digitalcollections.blueprints.webapp.springboot.controller;

import de.digitalcollections.blueprints.webapp.springboot.model.Person;
import java.util.Date;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class MainController {

  private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);

  @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
  public String printWelcome(Model model) {
    LOGGER.info("Homepage requested");
    model.addAttribute("time", new Date());
    model.addAttribute("person", new Person());
    return "main";
  }

  @RequestMapping(value = "/", method = RequestMethod.POST)
  public String create(@ModelAttribute @Valid Person person, BindingResult results, Model model, SessionStatus status, RedirectAttributes redirectAttributes) {
    if (results.hasErrors()) {
      redirectAttributes.addFlashAttribute("error", "invalid data");
      return "redirect:/";
    }
    model.addAttribute("time", new Date());
    model.addAttribute("person", person);
    return "main";
  }
}
