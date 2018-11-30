package de.digitalcollections.blueprints.rest.integration.tests;

import de.digitalcollections.blueprints.rest.client.HelloClient;
import de.digitalcollections.blueprints.rest.common.model.impl.Greeting;
import feign.Feign;
import feign.gson.GsonDecoder;
import java.io.File;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.from;

@Testcontainers
public class HelloClientTest {

  private static final String DOCKER_COMPOSE_FILE = "src/test/resources/docker-compose.yml";

  private static final String REST_SERVICE_NAME = "rest_1";

  private static final int REST_SERVICE_PORT = 9000;

//  //@ExtendWith(SpringExtension.class)
  @Container
  public static final DockerComposeContainer ENVIRONMENT = new DockerComposeContainer(new File(DOCKER_COMPOSE_FILE))
      .withLocalCompose(true)
      .withExposedService(REST_SERVICE_NAME, REST_SERVICE_PORT)
      .withTailChildContainers(true); // append all outputs of applications in the container to the main log

  @Test
  public void greetingShouldReturnGreetingForTheExpectedName() {
    final String targetUrl = String.format("http://%s:%d",
        ENVIRONMENT.getServiceHost(REST_SERVICE_NAME, REST_SERVICE_PORT),
        ENVIRONMENT.getServicePort(REST_SERVICE_NAME, REST_SERVICE_PORT));

    final HelloClient client = Feign.builder()
        .decoder(new GsonDecoder())
        .target(HelloClient.class, targetUrl);

    assertThat(client.greeting("Captain")).returns("Hello, Captain!", from(Greeting::getContent));
  }
}
