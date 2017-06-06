package de.digitalcollections.blueprints.rest.client;

import de.digitalcollections.blueprints.rest.common.model.impl.Greeting;
import feign.Feign;
import feign.gson.GsonDecoder;

public class Application {

  public static void main(String... args) {
    HelloClient client = Feign.builder()
            .decoder(new GsonDecoder())
            .target(HelloClient.class, "http://localhost:9000");

    final Greeting greeting = client.greeting("Sepp");
    System.out.println(greeting.getContent());
  }
}
