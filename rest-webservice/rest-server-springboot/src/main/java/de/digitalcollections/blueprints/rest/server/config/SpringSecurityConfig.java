package de.digitalcollections.blueprints.rest.server.config;

import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
            .antMatchers("/hello").permitAll()
            .antMatchers("/admin/**").authenticated()
            .and()
            .httpBasic();
  }

//  /**
//   * This section defines the user accounts which can be used for authentication
//   * as well as the roles each user has.
//   */
//  @Override
//  public void configure(AuthenticationManagerBuilder auth) throws Exception {
//
//    auth.inMemoryAuthentication()
//            .withUser("user1").password("password1").roles("USER").and()
//            .withUser("user2").password("password2").roles("USER");
//  }
}
