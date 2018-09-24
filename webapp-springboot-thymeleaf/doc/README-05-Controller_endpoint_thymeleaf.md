# Controller endpoint for displaying a Thymeleaf page

Finally we are adding our first webpage ;-)!
Our simple Thymeleaf page just renders the current date, labeled language specific.

## Controller

The following Spring MVC controller handles a `GET` request for `/` and returns a Date-model and the name of the Thymeleaf template:

File `src/main/java/de/digitalcollections/blueprints/webapp/springboot/controller/MainController.java`:

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

There are also shorter method specific annotations: `GetMapping`, `PostMapping`, `PutMapping` and `DeleteMapping`.
So we can skip the `method`attribute:

```java
package de.digitalcollections.blueprints.webapp.springboot.controller;

import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

  private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);

  @GetMapping(value = {"", "/"})
  public String printWelcome(Model model) {
    LOGGER.info("Homepage requested");
    model.addAttribute("time", new Date());
    return "main";
  }
}
```

Note: As the convenience annotation `@SpringBootApplication` adds a `@ComponentScan` for other components, configurations, and services in the current package (and subpackages) of the `Application` class, the `@Controller`annotated controller is automatically recognized.

Now the endpoint is mapped during startup of the application:

```
2018-09-24 08:54:25.692  INFO 10363 --- [main] s.w.s.m.m.a.RequestMappingHandlerMapping : Mapped "{[ || /],methods=[GET]}" onto public java.lang.String de.digitalcollections.blueprints.webapp.springboot.controller.MainController.printWelcome(org.springframework.ui.Model)
```

A request to the mapped url `http://localhost:9000/` still brings up a 404 page,
because the template with name "main" configured in the `printWelcome` method does not exist, yet.
But the logging info "Homepage requested" already shows up in our logging output:

```
2018-09-24 08:55:47.735  INFO 10363 --- [nio-9000-exec-1] d.d.b.w.s.controller.MainController      : Homepage requested
```

So let's add a template.

## Thymeleaf

We use [Thymeleaf](https://www.thymeleaf.org/) template engine, which is the de facto standard for Spring MVC.

### Dependencies

There is also a Spring Boot starter for Thymeleaf we add to our `pom.xml`:

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-thymeleaf</artifactId>
  <scope>runtime</scope>
</dependency>
```

As there is no Thymeleaf-Java code dependency (but only HTML-side code), scope `runtime`is sufficient.

Additionally we add the runtime-dependency for the [Thymeleaf layout dialect](https://ultraq.github.io/thymeleaf-layout-dialect/) that we use
to make reusable templates and fragments:

```xml
<dependency>
  <groupId>nz.net.ultraq.thymeleaf</groupId>
  <artifactId>thymeleaf-layout-dialect</artifactId>
  <scope>runtime</scope>
</dependency>
```

### Templates

#### Homepage template

As the controller returns the template name `main`, we have to provide a Thymeleaf template matching this.

A Spring Boot webapp has no webapp-directory to put the template there.
All resources have to be on the classpath by putting them into `src/main/resources`.
By convention the templates should be placed in the subdirectory `templates`:

File `src/main/resources/templates/main.html`:

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org"
      xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout"
      layout:decorate="~{base}">
  <body>
    <section layout:fragment="content">
      <h1>Webapp Blueprint (Spring Boot + Thymeleaf)</h1>
      <p>
        <label>Time:</label> <span th:text="${time}">...</span>
      </p>
    </section>
  </body>
</html>
```

Note that the time-value corresponds to the model-attribute provided in the controller by:

```java
model.addAttribute("time", new Date());
```

is pulled into the template using `th:text=${time}"`.
More on this in [Thymeleaf docs](https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#standard-expression-syntax).

This template is rather short as we use the Thymeleaf `decorate`-layout functionality to outsource the page skeleton to the base template named `base.html` (see html-tag on top).

#### Base template

Our basic bootstrap page skeleton `base.html`:

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org"
      xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout">
  <head>
    <meta charset="utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="" />
    <link rel="shortcut icon" th:href="@{/images/favicon.png}"/>

    <title>Webapp Blueprint (Spring Boot + Thymeleaf)</title>

    <!-- Bootstrap core CSS -->
    <link th:href="@{/css/bootstrap.min.css}" rel="stylesheet" />

    <!-- Custom styles for this template -->
    <link th:href="@{/css/starter-template.css}" rel="stylesheet" />
    <link th:href="@{/css/main.css}" rel="stylesheet" />

    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
    <!--[if lt IE 9]><script src="@{/js/ie8-responsive-file-warning.js}"></script><![endif]-->
    <script th:src="@{/js/ie-emulation-modes-warning.js}"></script>

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script th:src="@{/js/html5shiv.min.js}"></script>
    <script th:src="@{/js/respond.min.js}"></script>
    <![endif]-->
  </head>

  <body>

    <nav class="navbar navbar-inverse navbar-fixed-top">
      <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="">DigitalCollections</a>
        </div>
        <div id="navbar" class="collapse navbar-collapse">
          <ul class="nav navbar-nav">
            <li class="active"><a href="#">Home</a></li>
            <li><a href="?language=de">de</a> | <a href="?language=en">en</a></li>
          </ul>
        </div><!--/.nav-collapse -->
      </div>
    </nav>

    <div class="container">
      <section layout:fragment="content" class="starter-template">
        ...
      </section>

    </div>

    <script th:src="@{/js/jquery.min.js}"></script>
    <script th:src="@{/js/bootstrap.min.js}"></script>
    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script th:src="@{/js/ie10-viewport-bug-workaround.js}"></script>
  </body>
</html>
```

## Static files

By Spring Boot convention static files like javascript, css and images are automatically detected when placed in the `src/main/resources/static/{css,images,js}` subdirectories.

In Thymeleaf URL-attributes in link-, script- and img-tags have to be prefixed with `th:` and surrounded with `@{...}`.

Examples:

```html
<link rel="shortcut icon" th:href="@{/images/favicon.png}"/>
<link th:href="@{/css/bootstrap.min.css}" rel="stylesheet" />
<script th:src="@{/js/ie-emulation-modes-warning.js}"></script>
```

We put referenced static files `favicon.png`, `bootstrap.min.css`, `main.css`, `starter-template.css` and all js-files in their corresponding subdirectories under `src/main/resources/static`.

```
static
├── css
│   ├── bootstrap.min.css
│   ├── main.css
│   └── starter-template.css
├── images
│   └── favicon.png
└── js
    ├── bootstrap.min.js
    ├── html5shiv.min.js
    ├── ie10-viewport-bug-workaround.js
    ├── ie-emulation-modes-warning.js
    ├── jquery.min.js
    └── respond.min.js
```

We could start our server now and test the templates, but there would be errors because of our `@`-placeholder filtering of `src/main/resources`files...

## More restrictive resource filtering

Unfortunately the configuration for filtering resources (to replace placeholders in `application.yml`) gets in conflict with Thymeleaf syntax (also using `@` as part of its syntax).

To deactivate filtering for Thymeleaf templates and other resources that should not be filtered,
we could configure excludes in the plugin (option 1):

```xml
<plugin>
  <groupId>org.apache.maven.plugins</groupId>
  <artifactId>maven-resources-plugin</artifactId>
  <version>3.1.0</version>
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
But as you see you would have to remember to add new file types to the list whenever new file types are added to your webapp...

Another (more convenient and error preventing) option is to define two sections in the `resources`-element:

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
</build>
```

According to the first section (filtering=false) the build just copies all files in `resources` unparsed to the corresponding location in `target` build directory.
Afterwards the second section causes explicitely defined files (`includes`) to be parsed and then to be copied (overwriting their own unparsed companion of step 1).

After a `mvn clean install` (which does resources filtering during build), we can start up the server and open `http://localhost:9000/`.

Voila! Our first page renders!

![Homepage](./images/screenshot-thymeleaf-page.png)