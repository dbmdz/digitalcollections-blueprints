# DigitalCollections: Blueprints 3: REST Webservice Client (Frontend IMPL OpenFeign)

Part of the webservice blueprint is a REST webservice server providing a personal greeting message at his endpoint.
The endpoint's URL is http://localhost:9000/hello and provides a response like this:

```json
{
  "id": 2,
  "content": "Hello, Stranger!"
}
```

When passing a request param "name" (http://localhost:9000/hello?name=Sepp) you get a personalized greeting:

```json
{
  "id": 2,
  "content": "Hello, Sepp!"
}
```

In this webservice client blueprint we will be using the REST-client framework "OpenFeign" to use this webservice.

## OpenFeign

Homepage: <https://github.com/OpenFeign/feign>

### Installation

#### pom.xml

```xml
  <dependencies>
    <dependency>
      <groupId>io.github.openfeign</groupId>
      <artifactId>feign-core</artifactId>
      <version>9.5.0</version>
    </dependency>
    <dependency>
      <groupId>io.github.openfeign</groupId>
      <artifactId>feign-gson</artifactId>
      <version>9.5.0</version>
    </dependency>
  </dependencies>
```

### Basic Implementation

#### Application.java

```java
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
```

### Error Handling

#### Error if endpoint is down

```
Exception in thread "main" feign.RetryableException: Connection refused (Connection refused) executing GET http://localhost:9000/hello?name=Sepp
	at feign.FeignException.errorExecuting(FeignException.java:67)
	at feign.SynchronousMethodHandler.executeAndDecode(SynchronousMethodHandler.java:104)
	at feign.SynchronousMethodHandler.invoke(SynchronousMethodHandler.java:76)
	at feign.ReflectiveFeign$FeignInvocationHandler.invoke(ReflectiveFeign.java:103)
	at de.digitalcollections.blueprints.rest.client.frontend.impl.openfeign.$Proxy3.greeting(Unknown Source)
	at de.digitalcollections.blueprints.rest.client.frontend.impl.openfeign.Application.main(Application.java:22)
Caused by: java.net.ConnectException: Connection refused (Connection refused)
	at java.net.PlainSocketImpl.socketConnect(Native Method)
```

TODO