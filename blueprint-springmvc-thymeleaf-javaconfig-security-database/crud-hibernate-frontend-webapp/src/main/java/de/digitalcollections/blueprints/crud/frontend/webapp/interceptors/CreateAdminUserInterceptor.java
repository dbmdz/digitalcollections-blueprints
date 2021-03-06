package de.digitalcollections.blueprints.crud.frontend.webapp.interceptors;

import de.digitalcollections.blueprints.crud.business.api.service.UserService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Check if admin user exists before login dialog.
 */
public class CreateAdminUserInterceptor extends HandlerInterceptorAdapter implements MessageSourceAware {

  private MessageSource messageSource;

  private static final Logger LOGGER = LoggerFactory.getLogger(CreateAdminUserInterceptor.class);

  @Autowired
  private UserService userService;

  @Override
  public void setMessageSource(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    LOGGER.info("checking if admin user exists...");
    boolean activeAdminUserExists = userService.doesActiveAdminUserExist();
    if (!activeAdminUserExists) {
      request.setAttribute("createAdminUser", true);
    }
    return true;
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
          throws Exception {
    final Object doCreateAdminUser = request.getAttribute("createAdminUser");
    if (doCreateAdminUser != null) {
      boolean createAdminUser = (boolean) doCreateAdminUser;
      if (createAdminUser) {
        modelAndView.setView(new RedirectView("/setup/adminUser"));
        String message = messageSource.getMessage("msg.create_a_new_admin_user", null, LocaleContextHolder.getLocale());
        modelAndView.addObject("info_message", message);
        LOGGER.info("Admin user does not exist. Create a new administrator user.");
      }
    }
  }
}
