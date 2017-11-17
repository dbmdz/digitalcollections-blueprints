# Security: add authentication to service endpoints and authentication from client

In this chapter we secure a REST-API-Url with basic authentication using Spring Security. For demonstration purpose we add another Controller to the webservice application. The AdminController returns the server time when requested from Url `http://localhost:9000/admin/serverTime`:

File `src/main/java/de/digitalcollections/blueprints/rest/server/controller/AdminController.java`:

```java
package de.digitalcollections.blueprints.rest.server.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "The admin controller", name = "Admin controller")
@RestController
@RequestMapping("/admin")
public class AdminController {

  private final SimpleDateFormat sf = new SimpleDateFormat("MM/dd/YYYY HH:mm:ss");

  @ApiMethod(description = "return server time")
  @RequestMapping(value = "/serverTime", method = RequestMethod.GET)
  public String serverTime() {
    return sf.format(new Date());
  }
}
```

This API-endpoint "/admin/serverTime" is not yet secured.
 
## Server side security

see <https://spring.io/guides/gs/securing-web/>
see <https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-security.html>

### pom.xml

Add Spring Security dependency:

File `pom.xml`:

```xml
<dependencies>
  ...
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
  </dependency>
  ...
</dependencies>
```

"If Spring Security is on the classpath then web applications will be secure by default with ‘basic’ authentication on all HTTP endpoints. To add method-level security to a web application you can also add @EnableGlobalMethodSecurity with your desired settings. Additional information can be found in the [Spring Security Reference](http://docs.spring.io/spring-security/site/docs/4.2.3.RELEASE/reference/htmlsingle/#jc-method).

The default AuthenticationManager has a single user (‘user’ username, role `ANONYMOUS` and random password, printed at INFO level when the application starts up)"

When we run the application after adding the security dependency, we can see a line in the log output similar to this:

```sh
Using default security password: d215d394-eac4-461f-b42f-55acf83d2a0f
```

Now all endpoints are secured, even the "hello" endpoint that should be open/unsecured.
What we want is:

- Unsecured: http://localhost:9000/hello
- Secured: http://localhost:9000/admin/serverTime

### Spring Config

see <https://github.com/spring-projects/spring-data-examples/tree/master/rest/security#writing-a-security-policy>
see <https://github.com/spring-projects/spring-security/tree/4.2.3.RELEASE/samples/boot/helloworld>

"By default, when using Spring Boot, everything is locked down and a random password is generated. Usually, you will want to replace this with a user store of some kind. In addition to that, you need to configure method-level security."

To keep overall configuration simple we separate security configuration into an own configuration class in a subpackage `config` (which is scanned for spring components automatically in a spring boot application).

see <http://docs.spring.io/spring-security/site/docs/4.2.3.RELEASE/reference/htmlsingle/#jc-method>

"We can enable annotation-based security using the @EnableGlobalMethodSecurity annotation on any @Configuration instance." And we use "prePostEnabled" to use the new expression-based syntax."

By extending `WebSecurityConfigurerAdapter` we get access to overridable methods for customizing spring security configuration.

Basic security configuration file `src/main/java/de/digitalcollections/blueprints/rest/server/config/SpringSecurityConfig.java`:

```java
package de.digitalcollections.blueprints.rest.server.config;

import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

}
```

After adding this empty configuration, no authentication is required any more. The Spring Security defaults are overridden.

#### Unsecure public API endpoints and add HTTP-Basic authentication to secured API endpoints

To allow unsecured access to our `/hello` endpoint and HTTP-basic authentication secured access to all `/admin`-endpoints , we configure it by overriding security rules in the `configure`-method:

```java
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
}
```

Security is set up like this now:

- Unsecured: http://localhost:9000/hello
- Secured: http://localhost:9000/admin/serverTime (HTTP Basic, username = "user", password = "{see log output on startup}")

#### Adding specified user/password authentication

Until now there is just the user `user` with the randomly generated password.
Now we want define explicitely username/password-pairs used for http basic authentication.

There are two ways to configure this:

- override method `configure(AuthenticationManagerBuilder auth)`
- provide a UserDetailsServie bean

##### In-Memory user credentials

Example for the first (configure-method, using in memory credential data):

```java
...
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

  /**
   * This section defines the user accounts which can be used for authentication
   * as well as the roles each user has.
   */
  @Override
  public void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.inMemoryAuthentication()
            .withUser("user1").password("password1").roles("USER").and()
            .withUser("user2").password("password2").roles("USER");
  }
}
```

Note: Even if not used, yet, at minimum one role must be added to each user.

Example for the second (UserDetailsService, using in memory credential data):

```java
...
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
  ...

  @Bean
  @Override
  public UserDetailsService userDetailsService() {
    InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
    manager.createUser(User.withUsername("user1").password("password1").roles("USER").build());
    manager.createUser(User.withUsername("user2").password("password2").roles("USER").build());
    return manager;
  }
}
```

##### Separation of concerns: Externalize UserDetailsServiceImpl

Instead of instantiate the service bean inline in the configuration class, we separate services as own concern/layer in a subpackage `service.impl` and implement the UserDetailsService-interface in an own class:

File `src/main/java/de/digitalcollections/blueprints/rest/server/service/impl/UserDetailsServiceImpl.java`

```java
package de.digitalcollections.blueprints.rest.server.service.impl;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  private final InMemoryUserDetailsManager repository = new InMemoryUserDetailsManager();

  public UserDetailsServiceImpl() {
    repository.createUser(User.withUsername("user1").password("password1").roles("USER").build());
    repository.createUser(User.withUsername("user2").password("password2").roles("USER").build());
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return repository.loadUserByUsername(username);
  }

}
```

This makes it even easier to replace the source ("repository" InMemoryUserDetailsManager) of user data enclosed in the service, not visible/with consequences/code changes to consumers of the service. It is also easy to see now that you just have to implement the method `loadUserByUsername` to fulfill a Spring Security UserDetailsService.

Note: Again we use the automatic component scanning of Spring Boot that automatically scans for components in the package and subpackages of the `Application` class.

##### Make username/passwords externalized/configurable

see <http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html>

Until now the usernames and passwords are hardcoded in `UserDetailsServiceImpl`. Now we want to make them configurable.
We externalize credentials into a properties file, that can be passed as `Properties` instance to the constructor `InMemoryUserDetailsManager(Properties users)`.

For reading the properties file (e.g. from location `file:///local/users.properties`) we use `PropertiesLoaderUtils` and `UrlResource` from Spring Framework. The path to the properties file can be configured in `application.yml`:

```java
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

```

As you can see you can read property values from `application.properties/.yml` by adding `@Value` annotation for the configurable member variable `pathToUserProperties`. To make sure the value is set we use it in an 'afterPropertiesSet'-method (introduced with InitializingBean interface). If you try to use it directly in a default constructor you will experience that it is not set, yet (will be null).

File `src/main/resources/application.yml`:

```yml
...
# user "database" for local profile
custom:
  pathToUserProperties: classpath:users.properties
...
```

The users.properties entries must have (at a minimum) as value (comma separated) password and authorities

File `users.properties`:

```ini
user1=password1,USER
user2=password2,USER
```

And the `InMemoryUserDetailsManager` (found in `UserAttributeEditor` parsing class...) even supports more sophisticated property values per user:

```ini
user1=password1,USER
user2=password2,USER,ADMIN
user3=password3,USER,disabled
```

By adding `disabled` at any position after the password it is possible to deactivate an user account.
All other values after the password are interpreted as authorities.