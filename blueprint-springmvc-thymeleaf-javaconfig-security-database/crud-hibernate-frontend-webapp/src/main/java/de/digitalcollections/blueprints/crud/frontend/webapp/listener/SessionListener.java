package de.digitalcollections.blueprints.crud.frontend.webapp.listener;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Session configuration.
 */
public class SessionListener implements HttpSessionListener {

  private static final Logger LOGGER = LoggerFactory.getLogger(SessionListener.class);

  @Override
  public void sessionCreated(HttpSessionEvent event) {
    HttpSession session = event.getSession();
    LOGGER.info("==== Session " + session.getId() + " is created ====");
    session.setMaxInactiveInterval(10 * 60 * 60); // seconds: 36000s = 10h

    /*
        replaces web.xml config:
        <session-config>
            <session-timeout>600</session-timeout> <!-- minutes: 60 * 10 h -->
        </session-config>
     */
  }

  @Override
  public void sessionDestroyed(HttpSessionEvent event) {
    HttpSession session = event.getSession();
    LOGGER.info("==== Session " + session.getId() + " is destroyed ====");
  }
}
