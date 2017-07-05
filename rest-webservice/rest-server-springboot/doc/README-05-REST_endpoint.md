# REST-API (Controller)

A simple example of an API is a greeting endpoint that takes an optional name as parameter.
(see <https://spring.io/guides/gs/actuator-service/>)

## Requirements

The request endpoint should be under "/hello" and handle GET requests.

The response should look like this:

```json
{
  "id": 1,
  "content": "Hello, World!"
}
```

The id field is a unique identifier for the greeting, and content is the textual representation of the greeting.

## Model: Representation Class

To model the greeting representation, create a representation class (it is now moved to a "common"-maven-module):

File "src/main/java/de/digitalcollections/blueprints/rest/common/model/impl/Greeting.java":

```java
package de.digitalcollections.blueprints.rest.common.model.impl;

public class Greeting {

  private final long id;
  private final String content;

  public Greeting(long id, String content) {
    this.id = id;
    this.content = content;
  }

  public long getId() {
    return id;
  }

  public String getContent() {
    return content;
  }
}
```

## Frontend: Resource Controller

In Spring, REST endpoints are just Spring MVC controllers. The following Spring MVC controller handles a GET request for /hello-world and returns the Greeting resource:

File "src/main/java/de/digitalcollections/blueprints/rest/server/controller/GreetingController.java":

```java
package de.digitalcollections.blueprints.rest.server.frontend.impl.controller;

import de.digitalcollections.blueprints.rest.common.model.impl.Greeting;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/hello")
public class GreetingController {

  private static final String TEMPLATE = "Hello, %s!";
  private final AtomicLong counter = new AtomicLong();

  @RequestMapping(method = RequestMethod.GET)
  public @ResponseBody Greeting sayHello(@RequestParam(value = "name", required = false, defaultValue = "Stranger") String name) {
    return new Greeting(counter.incrementAndGet(), String.format(TEMPLATE, name));
  }
}
```

The key difference between a human-facing controller and a REST endpoint controller is in how the response is created. Rather than rely on a view (such as Thymeleaf or JSP) to render model data in HTML, an endpoint controller simply returns the data to be written directly to the body of the response.

The @ResponseBody annotation tells Spring MVC not to render a model into a view, but rather to write the returned object into the response body. It does this by using one of Spring’s message converters. Because Jackson 2 is in the classpath (see 'mvn dependency:tree'), this means that MappingJackson2HttpMessageConverter will handle the conversion of Greeting to JSON if the request’s Accept header specifies that JSON should be returned.

Note: As the convenience annotation @SpringBootApplication adds a @ComponentScan for other components, configurations, and services in the current package (and subpackages) of the Application class, the controller is automatically recognized.

Now the endpoint is mapped during startup of the application:

```
[2017-05-08 13:55:21,557  INFO] .annotation.RequestMappingHandlerMapping: 543 [main    ] - Mapped "{[/hello],methods=[GET]}" onto public de.digitalcollections.template.rest.model.impl.Greeting de.digitalcollections.blueprints.rest.server.controller.GreetingController.sayHello(java.lang.String)
```

## Request / Response

```sh
$ curl -i http://localhost:8080/hello?name=Joe
HTTP/1.1 200 
X-Application-Context: application:local
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Mon, 08 May 2017 12:01:24 GMT

{"id":5,"content":"Hello, Joe!"}
```
