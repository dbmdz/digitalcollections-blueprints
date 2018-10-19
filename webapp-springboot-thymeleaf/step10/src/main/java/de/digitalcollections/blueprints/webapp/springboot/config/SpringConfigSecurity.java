package de.digitalcollections.blueprints.webapp.springboot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.info.InfoEndpoint;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SpringConfigSecurity extends WebSecurityConfigurerAdapter {

  @Value("${javamelody.init-parameters.monitoring-path:/monitoring}")
  String javamelodyMonitoringPath;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
            .requestMatchers(EndpointRequest.to(InfoEndpoint.class, HealthEndpoint.class)).permitAll()
            .requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole("ENDPOINT_ADMIN")
            .and()
            .httpBasic();
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring().antMatchers(javamelodyMonitoringPath);
  }
}
