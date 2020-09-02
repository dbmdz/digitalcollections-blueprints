# Security

Our final security configuration for the blueprint webapp `SpringConfigSecurity.java`:

```java
public class SpringConfigSecurity extends WebSecurityConfigurerAdapter {
  
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
            .requestMatchers(EndpointRequest.to(InfoEndpoint.class, HealthEndpoint.class)).permitAll()
            .requestMatchers(EndpointRequest.to("version", "prometheus")).permitAll()
            .requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole("ENDPOINT_ADMIN")
            .and()
            .httpBasic();
  }
}
```

This configuration ensures:

- monitoring:
  - <http://localhost:9001/monitoring/info>, <http://localhost:9001/monitoring/health>, <http://localhost:9001/monitoring/version> and <http://localhost:9001/monitoring/prometheus> are public accessible,
  - all other actuator endpoints are only accessible over basic auth authentication,
- webapp: <http://localhost:9000> is unsecured and public accessible for all

## Migration Notes

### from Spring Boot 1.5.8 to Spring Boot 2.0.x

see
- <https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-2.0-Migration-Guide>
- <https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-Security-2.0>

"Spring Boot 2.0 does not provide separate auto-configuration for user-defined endpoints and actuator endpoints. When Spring Security is on the classpath, the auto-configuration secures all endpoints by default."

Nice. Problem: ALL endpoints are secured, even all your webapp controller endpoints and static resources (css, ...)...

Disabling security for user defined endpoints but not for actuator endpoints (except info and health):

```java
package de.digitalcollections.blueprints.webapp.springboot.config;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SpringConfigSecurity extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
            .authorizeRequests()
            .requestMatchers(EndpointRequest.to("info", "health")).permitAll()
            .requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole("ACTUATOR")
            .antMatchers("/**").permitAll()
            .and()
            .httpBasic();
  }

}
```

If you have also setup security for your webapp controllers, you have to explicitely allow access to static resources:

```java
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;

@Configuration
public class SpringConfigSecurityWebapp extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests().requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll();
    http.authorizeRequests().antMatchers("/api/**", "/setup/**").permitAll();
```

If you get weired exception like this

```sh
org.thymeleaf.exceptions.TemplateProcessingException: Error during execution of processor 'org.thymeleaf.spring5.processor.SpringActionTagProcessor'
...
Caused by: java.lang.IllegalStateException: Cannot create a session after the response has been committed
```

The root cause is csrf-Protection.
see <https://docs.spring.io/spring-security/site/docs/current/reference/html/csrf.html>

Disable CSRF or define a CSRF-TokenRepository:

```java
http.csrf().disable();

http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
```
