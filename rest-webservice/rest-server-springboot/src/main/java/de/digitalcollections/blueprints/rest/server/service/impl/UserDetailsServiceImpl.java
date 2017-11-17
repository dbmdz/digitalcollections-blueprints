package de.digitalcollections.blueprints.rest.server.service.impl;

import java.util.Properties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService, InitializingBean {

  @Value("${custom.pathToUserProperties}")
  private String pathToUserProperties;

  private InMemoryUserDetailsManager repository;

  @Override
  public void afterPropertiesSet() throws Exception {
    UrlResource resource = new UrlResource(pathToUserProperties);
    Properties users = PropertiesLoaderUtils.loadProperties(resource);
    repository = new InMemoryUserDetailsManager(users);
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return repository.loadUserByUsername(username);
  }
}
