package de.digitalcollections.blueprints.crud.frontend.webapp.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class LogSessionIdFilter implements Filter {

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpSession session = httpRequest.getSession();
    if (session != null) {
      MDC.put("sessionID", session.getId());
    }

    final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null) {
      Object principal = authentication.getPrincipal();
//            MDC.put("username", principal.getBenutzername());
    }
    chain.doFilter(request, response);
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
  }

  @Override
  public void destroy() {
  }
}
