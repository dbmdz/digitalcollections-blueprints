package de.digitalcollections.blueprints.rest.client;


import de.digitalcollections.blueprints.rest.common.model.impl.Greeting;
import feign.Param;
import feign.RequestLine;

public interface HelloClient {

  @RequestLine("GET /hello?name={name}")
  Greeting greeting(@Param("name") String name);

}
