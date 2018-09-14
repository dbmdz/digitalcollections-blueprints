# Security configuration (unsecured webpage, secured actuator)

Spring Boot Actuator let you monitor and interact with your application.
Spring Boot includes a number of built-in endpoints and lets you add your own. Each individual
endpoint can be enabled or disabled.

The following example enables the `shutdown` endpoint:

```yml
management.endpoint.shutdown.enabled=true
```

Since Endpoints may contain sensitive information, careful consideration should be given about when
to expose them. To change which endpoints are exposed, use the following technology-specific
`include` and `exclude` properties. `*` can be used to select all endpoints. For example, to expose
everything over HTTP except the env and beans endpoints, use the following properties:

```yml
management.endpoints.web.exposure.include=*
management.endpoints.web.exposure.exclude=env,beans
```

## `SpringConfigSecurity`

The following file must be created under `src/main/java/de.digitalcollections.blueprints.webapp.springboot/config/SpringConfigSecurity.java`:

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

More Information about `SpringConfigSecurity` can be found under <https://docs.spring.io/spring-security/site/docs/current/guides/html5/helloworld-javaconfig.html>.