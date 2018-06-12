package de.digitalcollections.blueprints.webapp.springboot;

import de.digitalcollections.blueprints.webapp.springboot.config.SpringConfigWeb;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.boot.actuate.autoconfigure.web.server.LocalManagementPort;


import static org.assertj.core.api.BDDAssertions.then;


/**
 * Basic integration tests for webapp endpoints.
 */

@ExtendWith(SpringExtension.class)
@SpringBootApplication
@SpringBootTest(classes = {Application.class, SpringConfigWeb.class},
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // set random webapp/server port
@TestPropertySource(properties = {"management.server.port=0"}) // set random management port
public class ApplicationTest {

  // "local" is not profile name, it is needed to use random port
  @LocalServerPort //("${local.server.port}")
  private int port;

  // "local" is not profile name, it is needed to use random port
  @LocalManagementPort //("${local.management.port}")
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
        "http://localhost:" + this.mgt + "/monitoring/env", Map.class);

    then(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  public void shouldReturn401WhenSendingUnauthorizedRequestToSensitiveManagementEndpoint() throws Exception {
    @SuppressWarnings("rawtypes")
    ResponseEntity<Map> entity = this.testRestTemplate.getForEntity(
        "http://localhost:" + this.mgt + "/monitoring/env", Map.class);

    then(entity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
  }
}