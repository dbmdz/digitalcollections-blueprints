package de.digitalcollections.blueprints.webapp.springboot;

import java.io.IOException;
import java.net.URI;
import java.util.Base64;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.web.server.LocalManagementPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import redis.embedded.RedisServer;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"management.server.port=0"}) // set random management port
public class ApplicationTest {

  @TestConfiguration
  public static class EmbeddedRedisTestConfiguration {

    private final RedisServer redisServer;

    public EmbeddedRedisTestConfiguration(@Value("${spring.redis.port}") final int redisPort, @Value("${spring.redis.password}") final String redisPassword) throws IOException {
      redisServer = RedisServer.builder().port(redisPort).setting("requirepass " + redisPassword).build();
    }

    @PostConstruct
    public void startRedis() {
      this.redisServer.start();
    }

    @PreDestroy
    public void stopRedis() {
      this.redisServer.stop();
    }
  }

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private RedisTemplate<String, String> redisTemplate;

  // "local" is not profile name, it is needed to use random port
  @LocalManagementPort //("${local.management.port}")
  private int monitoringPort;

  @Test
  public void shouldReturn401WhenSendingUnauthorizedRequestToSensitiveManagementEndpoint() throws Exception {
    @SuppressWarnings("rawtypes")
    ResponseEntity<Map> entity = this.restTemplate.getForEntity(
            "http://localhost:" + this.monitoringPort + "/monitoring/env", Map.class);

    assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
  }

  @Test
  public void ensureSessionIdDoesNotChangeWhichMeansThatSessionHandlingViaRedisWorks() throws Exception {
    URI uri = URI.create("http://localhost:" + this.monitoringPort + "/monitoring/env");

    // First request with authentication
    HttpHeaders headers1 = new HttpHeaders();
    headers1.set("Authorization", "Basic " + Base64.getEncoder().encodeToString("admin:secret".getBytes()));
    RequestEntity<Object> request1 = new RequestEntity(headers1, HttpMethod.GET, uri);
    ResponseEntity<String> response1 = restTemplate.exchange(request1, String.class);
    assertThat(response1.getStatusCode()).isEqualTo(HttpStatus.OK);
    String firstSessionId = response1.getBody();
    String cookie = response1.getHeaders().getFirst("Set-Cookie");

    // Second requests uses sessionId but does not authenticate again
    HttpHeaders headers2 = new HttpHeaders();
    headers2.set("Cookie", cookie);
    RequestEntity<Object> request2 = new RequestEntity(headers2, HttpMethod.GET, uri);
    ResponseEntity<String> response2 = restTemplate.exchange(request2, String.class);
    assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.OK);
    String secondSessionId = response2.getBody();

    assertThat(firstSessionId).isEqualTo(secondSessionId);
  }

}
