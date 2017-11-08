# Testing

In order to check if your application is functional you should write unit / integration tests for your application. Below you can find an example of such a test that checks:

- if your controller is responsive and unsecured
- if your management endpoint is responsive and secured

As you can see for tests we’re starting the application on a random port to get not in conflict with used ports on test machine. We are setting no 'spring.profiles.active' as we want to test production profile (which is the default, see application.yml).

Also make sure, that a logging configuration (logback-spring.xml) exists in test sources which does not depend on a set environment variable spring.profiles.active.

File "src/test/java/de/digitalcollections/blueprints/webapp/springboot/ApplicationTest.java":

```java
package de.digitalcollections.blueprints.webapp.springboot;

import de.digitalcollections.blueprints.webapp.springboot.config.SpringConfigWebSecurity;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.BDDAssertions.then;

/**
 * Basic integration tests for webapp endpoints.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class, SpringConfigWebSecurity.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // set random webapp/server port
@TestPropertySource(properties = {"management.port=0", "management.security.enabled=true"}) // set random management port
public class ApplicationTest {

  // "local" is not profile name, it is needed to use random port
  @Value("${local.server.port}")
  private int port;

  // "local" is not profile name, it is needed to use random port
  @Value("${local.management.port}")
  private int mgt;

  @Autowired
  private TestRestTemplate testRestTemplate;

  @Test
  public void shouldReturn200WhenSendingRequestToRoot() throws Exception {
    @SuppressWarnings("rawtypes")
    ResponseEntity<String> entity = this.testRestTemplate.getForEntity(
            "http://localhost:" + this.port + "/", String.class);

    then(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  public void shouldReturn200WhenSendingAuthorizedRequestToSensitiveManagementEndpoint() throws Exception {
    @SuppressWarnings("rawtypes")
    ResponseEntity<Map> entity = this.testRestTemplate.withBasicAuth("admin", "secret").getForEntity(
            "http://localhost:" + this.mgt + "/actuator/env", Map.class);

    then(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  public void shouldReturn401WhenSendingUnauthorizedRequestToSensitiveManagementEndpoint() throws Exception {
    @SuppressWarnings("rawtypes")
    ResponseEntity<Map> entity = this.testRestTemplate.getForEntity(
            "http://localhost:" + this.mgt + "/actuator/env", Map.class);

    then(entity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
  }

}
```

By using the annotation `@TestPropertySource` you can overwrite properties set in `application.yml`.

File "src/test/resources/logback-spring.xml":

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
  <appender name="default" class="ch.qos.logback.core.ConsoleAppender">
    <encoder>
      <pattern>[%d{ISO8601} %5p] %40.40c:%4L [%-8t] - %m%n</pattern>
    </encoder>
  </appender>

  <logger name="de.digitalcollections.blueprints" level="debug" />
    
  <root level="info">
    <appender-ref ref="default" />
  </root>
</configuration>
```
