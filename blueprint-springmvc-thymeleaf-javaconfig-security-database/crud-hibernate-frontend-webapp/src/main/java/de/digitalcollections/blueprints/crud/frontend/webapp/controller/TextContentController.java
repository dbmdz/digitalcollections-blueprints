package de.digitalcollections.blueprints.crud.frontend.webapp.controller;

import de.digitalcollections.blueprints.crud.business.api.service.TextContentService;
import de.digitalcollections.blueprints.crud.model.api.TextContent;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller for all "TextContent" pages.
 */
@Controller
@RequestMapping(value = {"/textcontent"})
@SessionAttributes(value = {"textContent"})
public class TextContentController extends AbstractController implements MessageSourceAware {

  private MessageSource messageSource;

  @Autowired
  private TextContentService textContentService;

  @Override
  public void setMessageSource(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @RequestMapping(value = "new", method = RequestMethod.GET)
  public String create(Model model) {
    model.addAttribute("textContent", textContentService.create());
    return "textcontent/new";
  }

  @RequestMapping(value = "new", method = RequestMethod.POST)
  public String create(@ModelAttribute @Valid TextContent textContent, BindingResult results, Model model, RedirectAttributes redirectAttributes) {
    //verifyBinding(results);
    if (results.hasErrors()) {
      return "textcontent/new";
    }
    textContentService.save(textContent);
    /*if (results.hasErrors()) {
      return "users/edit";
    }*/
    //status.setComplete();
    String message = messageSource.getMessage("msg.created_successfully", null, LocaleContextHolder.getLocale());
    redirectAttributes.addFlashAttribute("success_message", message);
    return "redirect:/";
  }

  @RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
  public String delete(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
    textContentService.delete(id);
    String message = messageSource.getMessage("msg.deleted_successfully", null, LocaleContextHolder.getLocale());
    redirectAttributes.addFlashAttribute("success_message", message);
    return "redirect:/";
  }
}
