# Initial Setup

The skeleton starts with an initial minimal setup of two files in a directory `my-webapp`:

```
my-webapp
 |
 |- pom.xml
 |- src
    |- main
       |- java
          |- de
             |- digitalcollections
                |- blueprints
                   |- webapp
                      |- springboot
                         |- Application.java
```

## Basic Maven project file `pom.xml`

Spring Boot starter is defined as parent like described here: <https://docs.spring.io/spring-boot/docs/current/reference/html/using-boot-build-systems.html#using-boot-maven-parent-pom>.

You only need to specify the desired Spring Boot version on this dependency. For all other Spring Boot starter dependencies, you can safely omit the version number:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.1.2.RELEASE</version>
  </parent>

  <name>DigitalCollections: Blueprints 4: Webapp (Spring Boot + Thymeleaf)</name>
  <groupId>de.digitalcollections.blueprints</groupId>
  <artifactId>webapp-springboot-thymeleaf</artifactId>
  <version>1.0.0-SNAPSHOT</version>
  <packaging>jar</packaging>

  <properties>
    <!-- The spring-boot-starter-parent chooses fairly conservative Java compatibility. If you want to follow our recommendation and use a later Java version you can add a java.version property -->
    <java.version>1.8</java.version>
    <maven.compiler.source>1.8</maven.compiler.source>
    <maven.compiler.target>1.8</maven.compiler.target>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
  </properties>

  <dependencies>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-test</artifactId>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
  </dependencies>

  <build>
    <plugins>
      <plugin>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
        <executions>
          <execution>
            <goals>
              <goal>repackage</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
    </plugins>
  </build>
</project>
```

The Spring Boot Maven plugin provides many convenient features:

- It collects all the jars on the classpath and builds a single, runnable "über-jar", which makes it more convenient to execute and transport your service.
- It searches for the `public static void main()` method to flag as a runnable class.
- It provides a built-in dependency resolver that sets the version number to match Spring Boot dependencies. You can override any version you wish, but it will default to Boot’s chosen set of versions.

see <https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html>

If you don’t include the `<execution/>` configuration as above, you can run the plugin on its own (but only if the package goal is used as well). For example:

```sh
$ cd my-webapp
$ mvn package spring-boot:repackage
$ ls target/*.jar
target/myproject-1.0.0.jar target/myproject-1.0.0.jar.original
```

In case you want to overlay/use the resulting Spring Boot JAR as dependency in another project, you have to add the classifier `exec` to the plugin's configuration:

```xml
<plugin>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-maven-plugin</artifactId>
  <executions>
    <execution>
      <goals>
        <goal>repackage</goal>
      </goals>
    </execution>
  </executions>
  <configuration>
    <classifier>exec</classifier>
  </configuration>
</plugin>
```

In this case two jar files are packaged:

- `target/webapp-springboot-thymeleaf-1.0.0-SNAPSHOT.jar` that can be used as dependency (but not be executed)
- `target/webapp-springboot-thymeleaf-1.0.0-SNAPSHOT-exec.jar` that can be executed with `java -jar`

see <http://docs.spring.io/spring-boot/docs/current/maven-plugin/examples/repackage-classifier.html>

There are three starters added as dependencies (see <http://docs.spring.io/spring-boot/docs/current/reference/html/using-boot-build-systems.html#using-boot-starter>):

- **spring-boot-starter-actuator**: Starter for using Spring Boot’s Actuator which provides production ready features to help you monitor and manage your application.
- **spring-boot-starter-test**: Starter for testing Spring Boot applications with libraries including JUnit, Hamcrest and Mockito.
- **spring-boot-starter-web**: Starter for building web, including RESTful, applications using Spring MVC. Uses Tomcat as the default embedded container.

## Basic `Application.java`

```java
package de.digitalcollections.blueprints.webapp.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

  public static void main(String[] args) throws Exception {
    SpringApplication.run(Application.class, args);
  }
}
```

The `@SpringBootApplication` annotation provides a load of defaults (like the embedded servlet container) depending on the contents of your classpath, and other things. It also turns on Spring MVC’s `@EnableWebMvc` annotation that activates web endpoints. `@SpringBootApplication` is a convenience annotation that adds all of the following:

 - `@Configuration` tags the class as a source of bean definitions for the application context.
 - `@EnableAutoConfiguration` tells Spring Boot to start adding beans based on classpath settings, other beans, and various property settings.
 - Normally you would add `@EnableWebMvc` for a Spring MVC app, but Spring Boot adds it automatically when it sees `spring-webmvc` on the classpath. This flags the application as a web application and activates key behaviors such as setting up a `DispatcherServlet`.
 - `@ComponentScan` tells Spring to look for other components, configurations, and services in the current package (and subpackages), allowing it to find controllers, too.

## Build a classic WAR file

see <https://spring.io/guides/gs/convert-jar-to-war/> and <http://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#build-tool-plugins-maven-packaging> and <http://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#howto-create-a-deployable-war-file>

## Build an executable JAR

see <https://spring.io/guides/gs/rest-service/>

You can build a single executable JAR file that contains all the necessary dependencies, classes, and resources, and run that. This makes it easy to ship, version, and deploy the service as an application throughout the development lifecycle, across different environments, and so forth.

```sh
$ cd my-webapp
$ mvn clean package
```

## Run the initial Webapp

- Using Maven:

```sh
$ cd my-webapp
$ mvn spring-boot:run
```

- Run executable jar (see above):

```sh
$ cd my-webapp
$ java -jar target/rest-server-springboot-1.0.0-SNAPSHOT-exec.jar
```

- NetBeans:

  * Right-click on `Application.java` (in Projects-View)
  * Click `Run File`

Logging output is displayed. The webapp should be up and running within a few seconds.

```
...
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v2.1.1.RELEASE)

2018-09-21 08:11:26.640  INFO 28266 --- [           main] d.d.b.webapp.springboot.Application      : Starting Application on ralf-linux with PID 28266 (/home/ralf/development/github.com/dbmdz/blueprints/blueprint4-springboot-webapp/step01/target/classes started by ralf in /home/ralf/development/github.com/dbmdz/blueprints/blueprint4-springboot-webapp/step01)
2018-09-21 08:11:26.642  INFO 28266 --- [           main] d.d.b.webapp.springboot.Application      : No active profile set, falling back to default profiles: default
2018-09-21 08:11:26.674  INFO 28266 --- [           main] ConfigServletWebServerApplicationContext : Refreshing org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext@75c072cb: startup date [Fri Sep 21 08:11:26 CEST 2018]; root of context hierarchy
2018-09-21 08:11:27.700  INFO 28266 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port(s): 8080 (http)
2018-09-21 08:11:27.722  INFO 28266 --- [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2018-09-21 08:11:27.722  INFO 28266 --- [           main] org.apache.catalina.core.StandardEngine  : Starting Servlet Engine: Apache Tomcat/8.5.34
2018-09-21 08:11:27.730  INFO 28266 --- [ost-startStop-1] o.a.catalina.core.AprLifecycleListener   : The APR based Apache Tomcat Native library which allows optimal performance in production environments was not found on the java.library.path: [/opt/jdk/jre/lib/amd64:/opt/jdk/jre/lib/i386::/usr/java/packages/lib/amd64:/usr/lib64:/lib64:/lib:/usr/lib]
2018-09-21 08:11:27.809  INFO 28266 --- [ost-startStop-1] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2018-09-21 08:11:27.810  INFO 28266 --- [ost-startStop-1] o.s.web.context.ContextLoader            : Root WebApplicationContext: initialization completed in 1139 ms
2018-09-21 08:11:28.155  INFO 28266 --- [ost-startStop-1] o.s.b.w.servlet.FilterRegistrationBean   : Mapping filter: 'characterEncodingFilter' to: [/*]
2018-09-21 08:11:28.156  INFO 28266 --- [ost-startStop-1] o.s.b.w.servlet.FilterRegistrationBean   : Mapping filter: 'webMvcMetricsFilter' to: [/*]
2018-09-21 08:11:28.156  INFO 28266 --- [ost-startStop-1] o.s.b.w.servlet.FilterRegistrationBean   : Mapping filter: 'hiddenHttpMethodFilter' to: [/*]
2018-09-21 08:11:28.156  INFO 28266 --- [ost-startStop-1] o.s.b.w.servlet.FilterRegistrationBean   : Mapping filter: 'httpPutFormContentFilter' to: [/*]
2018-09-21 08:11:28.156  INFO 28266 --- [ost-startStop-1] o.s.b.w.servlet.FilterRegistrationBean   : Mapping filter: 'requestContextFilter' to: [/*]
2018-09-21 08:11:28.156  INFO 28266 --- [ost-startStop-1] o.s.b.w.servlet.FilterRegistrationBean   : Mapping filter: 'httpTraceFilter' to: [/*]
2018-09-21 08:11:28.156  INFO 28266 --- [ost-startStop-1] o.s.b.w.servlet.ServletRegistrationBean  : Servlet dispatcherServlet mapped to [/]
2018-09-21 08:11:28.227  INFO 28266 --- [           main] o.s.w.s.handler.SimpleUrlHandlerMapping  : Mapped URL path [/**/favicon.ico] onto handler of type [class org.springframework.web.servlet.resource.ResourceHttpRequestHandler]
2018-09-21 08:11:28.401  INFO 28266 --- [           main] s.w.s.m.m.a.RequestMappingHandlerAdapter : Looking for @ControllerAdvice: org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext@75c072cb: startup date [Fri Sep 21 08:11:26 CEST 2018]; root of context hierarchy
2018-09-21 08:11:28.455  INFO 28266 --- [           main] s.w.s.m.m.a.RequestMappingHandlerMapping : Mapped "{[/error],produces=[text/html]}" onto public org.springframework.web.servlet.ModelAndView org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController.errorHtml(javax.servlet.http.HttpServletRequest,javax.servlet.http.HttpServletResponse)
2018-09-21 08:11:28.456  INFO 28266 --- [           main] s.w.s.m.m.a.RequestMappingHandlerMapping : Mapped "{[/error]}" onto public org.springframework.http.ResponseEntity<java.util.Map<java.lang.String, java.lang.Object>> org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController.error(javax.servlet.http.HttpServletRequest)
2018-09-21 08:11:28.474  INFO 28266 --- [           main] o.s.w.s.handler.SimpleUrlHandlerMapping  : Mapped URL path [/webjars/**] onto handler of type [class org.springframework.web.servlet.resource.ResourceHttpRequestHandler]
2018-09-21 08:11:28.474  INFO 28266 --- [           main] o.s.w.s.handler.SimpleUrlHandlerMapping  : Mapped URL path [/**] onto handler of type [class org.springframework.web.servlet.resource.ResourceHttpRequestHandler]
2018-09-21 08:11:28.652  INFO 28266 --- [           main] o.s.b.a.e.web.EndpointLinksResolver      : Exposing 2 endpoint(s) beneath base path '/actuator'
2018-09-21 08:11:28.658  INFO 28266 --- [           main] s.b.a.e.w.s.WebMvcEndpointHandlerMapping : Mapped "{[/actuator/health],methods=[GET],produces=[application/vnd.spring-boot.actuator.v2+json || application/json]}" onto public java.lang.Object org.springframework.boot.actuate.endpoint.web.servlet.AbstractWebMvcEndpointHandlerMapping$OperationHandler.handle(javax.servlet.http.HttpServletRequest,java.util.Map<java.lang.String, java.lang.String>)
2018-09-21 08:11:28.659  INFO 28266 --- [           main] s.b.a.e.w.s.WebMvcEndpointHandlerMapping : Mapped "{[/actuator/info],methods=[GET],produces=[application/vnd.spring-boot.actuator.v2+json || application/json]}" onto public java.lang.Object org.springframework.boot.actuate.endpoint.web.servlet.AbstractWebMvcEndpointHandlerMapping$OperationHandler.handle(javax.servlet.http.HttpServletRequest,java.util.Map<java.lang.String, java.lang.String>)
2018-09-21 08:11:28.659  INFO 28266 --- [           main] s.b.a.e.w.s.WebMvcEndpointHandlerMapping : Mapped "{[/actuator],methods=[GET],produces=[application/vnd.spring-boot.actuator.v2+json || application/json]}" onto protected java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.springframework.boot.actuate.endpoint.web.Link>> org.springframework.boot.actuate.endpoint.web.servlet.WebMvcEndpointHandlerMapping.links(javax.servlet.http.HttpServletRequest,javax.servlet.http.HttpServletResponse)
2018-09-21 08:11:28.691  INFO 28266 --- [           main] o.s.j.e.a.AnnotationMBeanExporter        : Registering beans for JMX exposure on startup
2018-09-21 08:11:28.751  INFO 28266 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path ''
2018-09-21 08:11:28.755  INFO 28266 --- [           main] d.d.b.webapp.springboot.Application      : Started Application in 2.349 seconds (JVM running for 2.663)
```

So the server is running on port 8080, but you haven’t defined any business endpoints yet.
Therefore browsing `http://localhost:8080/` gives you a 404-error. BUt server is already running.

## Migration Notes

### from Spring Boot 1.5.8 to Spring Boot 2.0.x

see
- <https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-2.0-Migration-Guide>
- <https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-Security-2.0>

### pom.xml

Upgrade Spring Boot version:

```xml
<dependencyManagement>
  <dependencies>
    <dependency>
      <!-- Import dependency management from Spring Boot -->
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-dependencies</artifactId>
      <version>2.1.1.RELEASE</version>
      <type>pom</type>
      <scope>import</scope>
    </dependency>
  </dependencies>
</dependencyManagement>
```

Add temporarily Migrator-dependency:

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-properties-migrator</artifactId>
  <scope>runtime</scope>
</dependency>
```

Upgrade Logback-Logstash-Encoder to version 5.2:

```xml
<properties>
  <version.logstash-logback-encoder>5.2</version.logstash-logback-encoder>
</properties>

<dependency>
  <groupId>net.logstash.logback</groupId>
  <artifactId>logstash-logback-encoder</artifactId>
  <version>${version.logstash-logback-encoder}</version>
  <scope>runtime</scope>
</dependency>
```

### application.yml

Rename properties:

```yml
management.context-path / management.server.servlet.context-path -> management.endpoints.web.base-path
management.port -> management.server.port
security.user.name -> spring.security.user.name
security.user.password -> spring.security.user.password
server.context-path / server.contextPath -> server.servlet.context-path
spring.http.multipart.file-size-threshold -> spring.servlet.multipart.file-size-threshold
spring.http.multipart.location -> spring.servlet.multipart.location
spring.http.multipart.max-file-size -> spring.servlet.multipart.max-file-size
spring.http.multipart.max-request-size -> spring.servlet.multipart.max-request-size
```

WARNING: Make sure to merge separate spring-sections into one! (otherwise only the last section is used)

Remove properties:

```yml
endpoints.hypermedia.enabled (Reason: Hypermedia support in the Actuator is no longer available.)
management.security.enabled (Reason: A global security auto-configuration is now provided.)
security.basic.enabled (Reason: The security auto-configuration is no longer customizable.)
security.headers.cache (Reason: The security auto-configuration is no longer customizable.)
```

New properties:

```yml
management:
  endpoints:
    web:
      exposure:
        include: '*'
```

