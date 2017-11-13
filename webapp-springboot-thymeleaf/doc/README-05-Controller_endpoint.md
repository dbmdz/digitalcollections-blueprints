# Controller endpoint for displaying a Thymeleaf page

Our simple Thymeleaf page just renders the current date, labeled language specific.

## Controller

The following Spring MVC controller handles a GET request for / and returns a Date-model and the name of the Thymeleaf template:

File "src/main/java/de/digitalcollections/blueprints/webapp/springboot/controller/MainController.java":

```java
package de.digitalcollections.blueprints.webapp.springboot.controller;

import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MainController {

  private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);

  @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
  public String printWelcome(Model model) {
    LOGGER.info("Homepage requested");
    model.addAttribute("time", new Date());
    return "main";
  }
}
```

Note: As the convenience annotation @SpringBootApplication adds a @ComponentScan for other components, configurations, and services in the current package (and subpackages) of the Application class, the controller is automatically recognized.

Now the endpoint is mapped during startup of the application:

```
[2017-10-16 15:48:16,651  INFO] .annotation.RequestMappingHandlerMapping: 543 [main    ] - Mapped "{[ || /],methods=[GET]}" onto public java.lang.String de.digitalcollections.blueprints.webapp.springboot.controller.MainController.printWelcome(org.springframework.ui.Model)
```

## Thymeleaf

### Templates

As the controller returns the template name "main", we have to provide a Thymeleaf template for this.

A Spring Boot webapp has no webapp-directory to put the template there.
All resources have to be on the classpath by putting them into "src/main/resources".
By convention the templates should be placed in the subdirectory "templates":

File `src/main/resources/templates/main.html`:

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org"
      xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout"
      layout:decorator="base">
  <body>
    <section layout:fragment="content">
      <h1>Webapp Blueprint (Spring Boot + Thymeleaf)</h1>
      <p th:text="|#{time}: ${time}|">...</p>
    </section>
  </body>
</html>
```

This template is rather short as we use the Thymeleaf decorator-layout functionality to outsource the page skeleton to the base template named "base.html". Have a look at the source code itself for the content in base.html.

For Thymeleaf we add the necessary dependencies

### Replace white label error page

By default we get the "Whitelabel error page" when an exception in the application occurs.
To replace it with an error page that uses our design / base template, we just place an error.html template in "src/main/resources/templates":

File `src/main/resources/templates/error.html`:

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org"
      xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout"
      layout:decorate="~{base}">
  <body>
    <section layout:fragment="content">

      <h3>Uuups!</h3>
      
      <h1>Error <span th:text="${ status }">404</span></h1>
      <p th:text="${error}">...</p>
      <p th:text="${message}">...</p>
      <p th:text="${path}">...</p>
    </section>
  </body>
</html>
```

### pom.xml (Dependencies)

```xml
<dependencies>
  <dependency>
    <groupId>nz.net.ultraq.thymeleaf</groupId>
    <artifactId>thymeleaf-layout-dialect</artifactId>
    <version>2.2.2</version>
  </dependency>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
  </dependency>
  <dependency>
    <groupId>org.thymeleaf</groupId>
    <artifactId>thymeleaf</artifactId>
    <version>3.0.7.RELEASE</version>
  </dependency>
  <dependency>
    <groupId>org.thymeleaf</groupId>
    <artifactId>thymeleaf-spring4</artifactId>
    <version>3.0.7.RELEASE</version>
  </dependency>
...
```

As the Spring Boot starter for Thymeleaf provided older versions, we added explicitely versions.
(see https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#howto-use-thymeleaf-3)

To avoid warnings about deprecated thymelaf template mode, we add to the application.yml configuration:

```yml
spring:
  thymeleaf:
    mode: HTML
```

### pom.xml (resource filtering)

Unfortunately the configuration for filtering resources (to replace placeholders in application.yml) gets in conflict with Thymeleaf syntax (also using "@" as part of its syntax).

To deactivate filtering for Thymeleaf templates and other resources that should not be filtered,
we configure excludes in the plugin (option 1):

```xml
<plugin>
  <groupId>org.apache.maven.plugins</groupId>
  <artifactId>maven-resources-plugin</artifactId>
  <version>3.0.2</version>
  <configuration>
    <delimiters>
      <delimiter>@</delimiter>
    </delimiters>
    <useDefaultDelimiters>false</useDefaultDelimiters>
    <nonFilteredFileExtensions>
      <nonFilteredFileExtension>css</nonFilteredFileExtension>
      <nonFilteredFileExtension>gif</nonFilteredFileExtension>
      <nonFilteredFileExtension>html</nonFilteredFileExtension>
      <nonFilteredFileExtension>jpg</nonFilteredFileExtension>
      <nonFilteredFileExtension>js</nonFilteredFileExtension>
      <nonFilteredFileExtension>png</nonFilteredFileExtension>
    </nonFilteredFileExtensions>
  </configuration>
</plugin>
```

Another (more convenient and error preventing) option is to define two sections in the resources-element:

```xml
<build>
  <resources>
    <resource>
      <directory>src/main/resources</directory>
      <filtering>false</filtering>
    </resource>
    <resource>
      <directory>src/main/resources</directory>
      <filtering>true</filtering>
      <includes>
        <include>application.yml</include>
      </includes>
    </resource>
  </resources>
```

## Web Security



## Request / Response

```sh
$  curl -i localhost:9000
HTTP/1.1 401 
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
X-Frame-Options: DENY
Strict-Transport-Security: max-age=31536000 ; includeSubDomains
WWW-Authenticate: Basic realm="Spring"
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Mon, 16 Oct 2017 14:27:51 GMT
Server: Webapp Blueprint v1.0.0-SNAPSHOT

{"timestamp":1508164071748,"status":401,"error":"Unauthorized","message":"Full authentication is required to access this resource","path":"/"}
```

Unfortunately the securing of the actuator endpoints also lead to a secured webpage controller...

```sh
$ curl -u admin:secret localhost:9000
```

returns the webpage.

To solve this issue, read next step.
