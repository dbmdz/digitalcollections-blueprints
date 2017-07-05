package de.digitalcollections.blueprints.rest.server.service.impl;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  private final InMemoryUserDetailsManager instance = new InMemoryUserDetailsManager();

  public UserDetailsServiceImpl() {
    instance.createUser(User.withUsername("user1").password("password1").roles("USER").build());
    instance.createUser(User.withUsername("user2").password("password2").roles("USER").build());
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return instance.loadUserByUsername(username);
  }

}
