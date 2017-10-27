package de.digitalcollections.blueprints.webapp.springboot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SpringConfigWebSecurity extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // this config causes endpoints of webapp to be not secured through actuator security (what is wanted behaviour).

    // and this line is needed so that http basic authentication with configured username and password (in application.yml) is working again
    http.authorizeRequests().antMatchers("/actuator").authenticated().and().httpBasic();
  }
}
