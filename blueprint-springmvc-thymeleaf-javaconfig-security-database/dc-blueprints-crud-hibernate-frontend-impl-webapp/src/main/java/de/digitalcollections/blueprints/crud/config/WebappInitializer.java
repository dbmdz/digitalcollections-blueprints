package de.digitalcollections.blueprints.crud.config;

import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import de.digitalcollections.blueprints.crud.frontend.webapp.filter.LogSessionIdFilter;
import de.digitalcollections.blueprints.crud.frontend.webapp.listener.SessionListener;
import org.springframework.core.annotation.Order;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * Replaces web.xml.
 */
@Order(2)
public class WebappInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

  @Override
  protected Class<?>[] getRootConfigClasses() {
    return new Class<?>[]{SpringConfig.class};
  }

  @Override
  protected Class<?>[] getServletConfigClasses() {
    return null;
  }

  @Override
  protected String[] getServletMappings() {
    return new String[]{"/"};
  }

  @Override
  protected Filter[] getServletFilters() {
    // jpa session
    OpenEntityManagerInViewFilter openEntityManagerInViewFilter = new OpenEntityManagerInViewFilter();
    openEntityManagerInViewFilter.setEntityManagerFactoryBeanName("entityManagerFactory");

    // session id for logging, see log4j.xml
    final LogSessionIdFilter logSessionIdFilter = new LogSessionIdFilter();

    return new Filter[]{logSessionIdFilter, openEntityManagerInViewFilter};
  }

  @Override
  public void onStartup(ServletContext servletContext) throws ServletException {
    super.onStartup(servletContext);
    servletContext.addListener(new SessionListener());
  }
}
