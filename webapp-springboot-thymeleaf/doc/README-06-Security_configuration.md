# Security configuration (unsecured webpage, secured actuator)

Unfortunately Spring Boot Actuator security secures also the webpage which should be public accessible.

To solve this, we introduce an own security configuration class:

File "src/main/java/de/digitalcollections/blueprints/webapp/springboot/config/SpringConfigWebSecurity.java":

```java
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
    http.authorizeRequests().antMatchers("/monitoring").authenticated().and().httpBasic();
  }
}
```

How did we find this solution?

First we just added this class with an empty 'configure'-method. This lead to the wanted result of unsecuring the '/'-webpage and still authentication for the '/monitoring'-endpoint. But configured username and password (in application.yml) no longer worked. So endpoint secured without being able to authenticate...

After adding 'httpBasic()'-authentication for '/monitoring'-endpoint this worked again.

(If you have better solution: feel free to contact us ;-) )
