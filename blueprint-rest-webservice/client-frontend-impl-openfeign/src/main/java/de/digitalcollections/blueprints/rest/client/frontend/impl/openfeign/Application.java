package de.digitalcollections.blueprints.rest.client.frontend.impl.openfeign;

import de.digitalcollections.blueprints.rest.common.model.impl.Greeting;
import feign.Feign;
import feign.Param;
import feign.RequestLine;
import feign.gson.GsonDecoder;

public class Application {

  interface HelloEndpoint {

    @RequestLine("GET /hello?name={name}")
    Greeting greeting(@Param("name") String name);
  }

  public static void main(String... args) {
    HelloEndpoint endpoint = Feign.builder()
            .decoder(new GsonDecoder())
            .target(HelloEndpoint.class, "http://localhost:9000");

    final Greeting greeting = endpoint.greeting("Sepp");
    System.out.println(greeting.getContent());
  }
}
