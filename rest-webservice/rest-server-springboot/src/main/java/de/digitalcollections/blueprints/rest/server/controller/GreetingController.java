package de.digitalcollections.blueprints.rest.server.controller;

import de.digitalcollections.blueprints.rest.common.model.impl.Greeting;
import java.util.concurrent.atomic.AtomicLong;
import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.annotation.ApiQueryParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "The greeting controller", name = "Greeting controller")
@RestController
@RequestMapping("/hello")
public class GreetingController {

  private static final String TEMPLATE = "Hello, %s!";
  private final AtomicLong counter = new AtomicLong();

  @ApiMethod(description = "say hello to given name")
  @RequestMapping(method = RequestMethod.GET)
  public Greeting sayHello(
          @ApiQueryParam(
                  name = "name", description = "Name of the person to be used in greeting",
                  defaultvalue = "Stranger", required = false)
          @RequestParam(value = "name", required = false, defaultValue = "Stranger") String name) {
    return new Greeting(counter.incrementAndGet(), String.format(TEMPLATE, name));
  }
}
