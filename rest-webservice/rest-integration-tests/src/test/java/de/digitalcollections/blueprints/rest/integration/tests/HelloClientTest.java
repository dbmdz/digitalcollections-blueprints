package de.digitalcollections.blueprints.rest.integration.tests;

import de.digitalcollections.blueprints.rest.client.HelloClient;
import de.digitalcollections.blueprints.rest.common.model.impl.Greeting;
import feign.Feign;
import feign.gson.GsonDecoder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.from;

public class HelloClientTest {

  @Test
  public void greetingShouldReturnGreetingForTheExpectedName() {
    final String host = getEnvOrDefault("REST_HOST", "localhost");
    final String port = getEnvOrDefault("REST_PORT", "32790");

    final String targetUrl = String.format("http://%s:%s", host, port);

    final HelloClient client = Feign.builder()
                                    .decoder(new GsonDecoder())
                                    .target(HelloClient.class, targetUrl);

    assertThat(client.greeting("Captain")).returns("Hello, Captain!", from(Greeting::getContent));
  }

  private String getEnvOrDefault(String name, String defaultValue) {
    String value = System.getenv(name);
    if (value == null) {
      return defaultValue;
    } else {
      return value;
    }
  }
}
