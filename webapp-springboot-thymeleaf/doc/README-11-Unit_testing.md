# Testing

See <https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-testing.html#boot-features-testing-spring-boot-applications-testing-with-running-server>

In order to check if your application is functional you should write unit / integration tests for your application.
Below you can find an example of such JUnit 5 tests that check:

- if your controller is responsive and unsecured
- if your management endpoint is responsive and secured

## Add test dependencies

For JUnit5 we add the following dependencies and build plugins to our `pom.xml` (and exclude JUnit4 dependencies):

```xml
<properties>
  <version.maven-failsafe-plugin>2.22.1</version.maven-failsafe-plugin>
  <version.maven-surefire-plugin>2.22.1</version.maven-surefire-plugin>
</properties>

<dependencies>
  <dependency>
    <!-- Provide JUnit 5 API -->
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-api</artifactId>
    <scope>test</scope>
  </dependency>
  <dependency>
    <!-- and the engine for surefire and failsafe -->
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-engine</artifactId>
    <scope>test</scope>
  </dependency>
  ...
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
    <!-- Exclude JUnit 4 -->
    <exclusions>
      <exclusion>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
      </exclusion>
    </exclusions>
  </dependency>
</dependencies>

<build>
  <plugins>
    ...
    <plugin>
      <!-- Spring Boot configures surefire by default, but not failsafe -->
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-failsafe-plugin</artifactId>
      <version>${version.maven-failsafe-plugin}</version>
    </plugin>
    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-surefire-plugin</artifactId>
      <version>${version.maven-surefire-plugin}</version>
    </plugin>
  </plugins>
</build>
```

## Endpoint testing

For (integration) tests we’re starting the application on a random port to get not in conflict with used ports on test machine.

First simple Test (calling homepage = "/" url) `src/test/java/de/digitalcollections/blueprints/webapp/springboot/ApplicationTest.java`:

```java
package de.digitalcollections.blueprints.webapp.springboot;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ApplicationTest {

  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  public void shouldReturn200WhenSendingRequestToRoot() throws Exception {
    ResponseEntity<String> entity = this.restTemplate.getForEntity("/", String.class);
    assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

}
```

For testing monitoring-endpoint, we need a random port for actuator, too.
This is done by adding

```java
@TestPropertySource(properties = {"management.server.port=0"}) // set random management port
```

We extend our test to test authenticated and unauthenticated access to monitoring-endpoints:

```java
package de.digitalcollections.blueprints.webapp.springboot;

import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.web.server.LocalManagementPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"management.server.port=0"}) // set random management port
public class ApplicationTest {

  @Autowired
  private TestRestTemplate restTemplate;

  // "local" is not profile name, it is needed to use random port
  @LocalManagementPort //("${local.management.port}")
  private int monitoringPort;

  @Test
  public void shouldReturn200WhenSendingRequestToRoot() throws Exception {
    ResponseEntity<String> entity = this.restTemplate.getForEntity("/", String.class);
    assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  public void shouldReturn200WhenSendingRequestToManagementEndpoint() throws Exception {
    ResponseEntity<Map> entity = this.restTemplate.getForEntity("http://localhost:" + this.monitoringPort + "/monitoring/health", Map.class);

    assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  public void shouldReturn200WhenSendingAuthorizedRequestToSensitiveManagementEndpoint() throws Exception {
    ResponseEntity<Map> entity = this.restTemplate.withBasicAuth("admin", "secret").getForEntity(
            "http://localhost:" + this.monitoringPort + "/monitoring/env", Map.class);

    assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  public void shouldReturn401WhenSendingUnauthorizedRequestToSensitiveManagementEndpoint() throws Exception {
    @SuppressWarnings("rawtypes")
    ResponseEntity<Map> entity = this.restTemplate.getForEntity(
            "http://localhost:" + this.monitoringPort + "/monitoring/env", Map.class);

    assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
  }
}
```

By using the annotation `@TestPropertySource` you can overwrite properties set in `application.yml`.

## Migration Notes

### from JUnit 4 to JUnit 5

Rename test properties:

```java
@TestPropertySource(properties = {"management.port=0" ...}) -> @TestPropertySource(properties = {"management.server.port=0" ...})
```

Add to pom.xml

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-actuator-autoconfigure</artifactId>
</dependency>
```

Change:

```java
@Value("${local.management.port}") -> @LocalManagementPort
@Value("${local.server.port}") -> @LocalServerPort
```

Remove test properties:

```java
@TestPropertySource(properties = {..., "management.security.enabled=true"})
```

Add to pom.xml:

```xml
<dependency>
  <groupId>org.junit.jupiter</groupId>
  <artifactId>junit-jupiter-api</artifactId>
  <scope>test</scope>
</dependency>
```

Exclude Junit 4 dependency:

```
<dependency>
      <groupId>com.googlecode.json-simple</groupId>
      <artifactId>json-simple</artifactId>
      <exclusions>
        <exclusion>
          <groupId>junit</groupId>
          <artifactId>junit</artifactId>
        </exclusion>
      </exclusions>
    </dependency>
 <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-test</artifactId>
      <scope>test</scope>
      <exclusions>
        <exclusion>
          <groupId>junit</groupId>
          <artifactId>junit</artifactId>
        </exclusion>
      </exclusions>
    </dependency>
```
Replace:

```
@RunWith(SpringRunner.class) -> @ExtendWith(SpringExtension.class)
@BeforeClass -> @BeforeAll
@Before -> @BeforeEach
org.junit.Test -> org.junit.jupiter.api.Test
@Ignore -> @Disabled
```

Replace JUnit Exception tests:

```java
old:

@Test(expected = CliException.class) { ... }

new:

import static org.assertj.core.api.Assertions.assertThatThrownBy;

assertThatThrownBy(() -> {
  new Cli(printWriter, "--rules=doesnothexist.yml");
}).isInstanceOf(CliException.class);
```

Update to the latest version of `maven-surefire-plugin`:

```xml
<version.maven-surefire-plugin>2.22.1</version.maven-surefire-plugin>
```

And add the following `plugin` section:

```xml
<plugin>
  <artifactId>maven-surefire-plugin</artifactId>
  <version>${version.maven-surefire-plugin}</version>
</plugin>
```
