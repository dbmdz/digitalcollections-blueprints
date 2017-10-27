# Testing

In order to check if your application is functional you should write unit / integration tests of your application. Below you can find an example of such a test that checks:

- if your controller is responsive
- if your management endpoint is responsive

As you can see for tests we’re starting the application on a random port.
Also make sure, that a logging configuration (logback-spring.xml) exists in test sources which does not depend on a set environment variable spring.profiles.active.

File "src/test/java/de/digitalcollections/blueprints/rest/server/ApplicationTest.java":

```java
package de.digitalcollections.blueprints.rest.server;

import java.util.Map;
import static org.assertj.core.api.BDDAssertions.then;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Basic integration tests for service application.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
  "management.port=0",
  "pathToUserProperties=classpath:///users.properties"
})
public class ApplicationTest {

  @LocalServerPort
  private int port;

  @Value("${local.management.port}")
  private int mgt;

  @Autowired
  private TestRestTemplate testRestTemplate;

  @Test
  public void shouldReturn200WhenSendingRequestToController() throws Exception {
    @SuppressWarnings("rawtypes")
    ResponseEntity<Map> entity = this.testRestTemplate.getForEntity(
            "http://localhost:" + this.port + "/hello", Map.class);

    then(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  public void shouldReturn200WhenSendingRequestToManagementEndpoint() throws Exception {
    @SuppressWarnings("rawtypes")
    ResponseEntity<Map> entity = this.testRestTemplate.getForEntity(
            "http://localhost:" + this.mgt + "/info", Map.class);

    then(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
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
    
  <root level="info">
    <appender-ref ref="default" />
  </root>
</configuration>
```
