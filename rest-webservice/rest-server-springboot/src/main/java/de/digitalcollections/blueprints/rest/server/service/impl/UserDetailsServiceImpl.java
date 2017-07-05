package de.digitalcollections.blueprints.rest.server.service.impl;

import java.io.IOException;
import java.util.Properties;
import javax.annotation.PostConstruct;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

@ConfigurationProperties
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  private String pathToUserProperties;

  private InMemoryUserDetailsManager repository;

  @PostConstruct
  public void initialize() throws IOException {
    UrlResource resource = new UrlResource(pathToUserProperties);
    Properties users = PropertiesLoaderUtils.loadProperties(resource);
    repository = new InMemoryUserDetailsManager(users);
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return repository.loadUserByUsername(username);
  }

  public void setPathToUserProperties(String pathToUserProperties) {
    this.pathToUserProperties = pathToUserProperties;
  }
}
