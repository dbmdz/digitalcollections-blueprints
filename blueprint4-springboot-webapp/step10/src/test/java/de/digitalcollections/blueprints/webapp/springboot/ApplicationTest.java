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
