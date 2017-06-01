# Initial Setup

The skeleton starts with an initial minimal setup of two files (see <https://github.com/spring-guides/gs-actuator-service/tree/master/initial>).

```
pom.xml
src/main/java/de/digitalcollections/template/rest/server/frontend/impl/springboot/Application.java
```

## Basic pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>1.5.3.RELEASE</version>
    </parent>

    <groupId>de.digitalcollections.template</groupId>
    <artifactId>template-rest-server-frontend-impl-springboot</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <name>Template Project: REST-Server (Frontend IMPL Spring Boot)</name>
    <packaging>jar</packaging>

    <properties>
        <java.version>1.8</java.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

As we do not want to have Spring Boot as parent (we have another one), we modify the pom.xml like this:
(see <http://docs.spring.io/spring-boot/docs/current/reference/html/using-boot-build-systems.html#using-boot-maven-without-a-parent>):

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <parent>
    <groupId>de.digitalcollections.template</groupId>
    <artifactId>template-rest-server</artifactId>
    <version>1.0.0-SNAPSHOT</version>
  </parent>

  <groupId>de.digitalcollections.template</groupId>
  <artifactId>template-rest-server-frontend-impl-springboot</artifactId>
  <version>1.0.0-SNAPSHOT</version>
  <name>Template Project: REST-Server (Frontend IMPL Spring Boot)</name>
  <packaging>jar</packaging>

  <properties>
    <!-- The spring-boot-starter-parent chooses fairly conservative Java compatibility. If you want to follow our recommendation and use a later Java version you can add a java.version property -->
    <java.version>1.8</java.version>
  </properties>

  <dependencyManagement>
    <dependencies>
      <dependency>
        <!-- Import dependency management from Spring Boot -->
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-dependencies</artifactId>
        <version>1.5.3.RELEASE</version>
        <type>pom</type>
        <scope>import</scope>
      </dependency>
    </dependencies>
  </dependencyManagement>

  <dependencies>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-test</artifactId>
      <scope>test</scope>
    </dependency>
  </dependencies>

  <build>
    <plugins>
      <plugin>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
        <version>1.5.3.RELEASE</version>
      </plugin>
    </plugins>
  </build>
</project>
```
By using spring-boot-dependencies this way you can still keep the benefit of the Spring Boot dependency management, but not the plugin management. Therefore we also had to introduce the version for the spring-boot-maven-plugin.

The Spring Boot Maven plugin provides many convenient features:

- It collects all the jars on the classpath and builds a single, runnable "über-jar", which makes it more convenient to execute and transport your service.
- It searches for the public static void main() method to flag as a runnable class.
- It provides a built-in dependency resolver that sets the version number to match Spring Boot dependencies. You can override any version you wish, but it will default to Boot’s chosen set of versions.

There are three starters added as dependencies (see <http://docs.spring.io/spring-boot/docs/current/reference/html/using-boot-build-systems.html#using-boot-starter>):

- **spring-boot-starter-web**: Starter for building web, including RESTful, applications using Spring MVC. Uses Tomcat as the default embedded container.
- **spring-boot-starter-actuator**: Starter for using Spring Boot’s Actuator which provides production ready features to help you monitor and manage your application.
- **spring-boot-starter-test**: Starter for testing Spring Boot applications with libraries including JUnit, Hamcrest and Mockito.

## Basic Application.java

```java
package de.digitalcollections.template.rest.server.frontend.impl.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

  public static void main(String[] args) throws Exception {
    SpringApplication.run(Application.class, args);
  }
}
```

The @SpringBootApplication annotation provides a load of defaults (like the embedded servlet container) depending on the contents of your classpath, and other things. It also turns on Spring MVC’s @EnableWebMvc annotation that activates web endpoints. @SpringBootApplication is a convenience annotation that adds all of the following:

 - @Configuration tags the class as a source of bean definitions for the application context.
 - @EnableAutoConfiguration tells Spring Boot to start adding beans based on classpath settings, other beans, and various property settings.
 - Normally you would add @EnableWebMvc for a Spring MVC app, but Spring Boot adds it automatically when it sees spring-webmvc on the classpath. This flags the application as a web application and activates key behaviors such as setting up a DispatcherServlet.
 - @ComponentScan tells Spring to look for other components, configurations, and services in the current package (and subpackages), allowing it to find controllers, too.

## Build a classic WAR file

see <https://spring.io/guides/gs/convert-jar-to-war/> and <http://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#build-tool-plugins-maven-packaging> and <http://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#howto-create-a-deployable-war-file>

## Build an executable JAR

see <https://spring.io/guides/gs/rest-service/>

You can build a single executable JAR file that contains all the necessary dependencies, classes, and resources, and run that. This makes it easy to ship, version, and deploy the service as an application throughout the development lifecycle, across different environments, and so forth.

```sh
$ mvn clean package
```

## Run the initial Web Service

- Using Maven:

```sh
$ mvn spring-boot:run
```

- Run executable jar (see above):

```sh
$ java -jar target/template-rest-server-frontend-impl-springboot-1.0.0-SNAPSHOT.jar
```

Logging output is displayed. The service should be up and running within a few seconds.

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v1.5.3.RELEASE)

[2017-05-04 14:18:15,394  INFO] ver.frontend.impl.springboot.Application:  48 [main    ] - Starting Application on ralf-linux with PID 17101 (/home/ralf/DEV/SOURCES/template-rest-server/template-rest-server-frontend-impl-springboot/target/classes started by ralf in /home/ralf/DEV/SOURCES/template-rest-server/template-rest-server-frontend-impl-springboot)
[2017-05-04 14:18:15,398 DEBUG] ver.frontend.impl.springboot.Application:  51 [main    ] - Running with Spring Boot v1.5.3.RELEASE, Spring v4.3.8.RELEASE
[2017-05-04 14:18:15,399  INFO] ver.frontend.impl.springboot.Application: 641 [main    ] - The following profiles are active: local
[2017-05-04 14:18:15,457  INFO] ationConfigEmbeddedWebApplicationContext: 582 [main    ] - Refreshing org.springframework.boot.context.embedded.AnnotationConfigEmbeddedWebApplicationContext@52aa2946: startup date [Thu May 04 14:18:15 CEST 2017]; root of context hierarchy
[2017-05-04 14:18:15,773  INFO] ibernate.validator.internal.util.Version:  30 [background-preinit] - HV000001: Hibernate Validator 5.3.5.Final
[2017-05-04 14:18:16,508  INFO] ed.tomcat.TomcatEmbeddedServletContainer:  89 [main    ] - Tomcat initialized with port(s): 8080 (http)
[2017-05-04 14:18:16,520  INFO] org.apache.catalina.core.StandardService: 179 [main    ] - Starting service Tomcat
[2017-05-04 14:18:16,521  INFO]  org.apache.catalina.core.StandardEngine: 179 [main    ] - Starting Servlet Engine: Apache Tomcat/8.5.14
[2017-05-04 14:18:16,612  INFO] e.ContainerBase.[Tomcat].[localhost].[/]: 179 [localhost-startStop-1] - Initializing Spring embedded WebApplicationContext
[2017-05-04 14:18:16,612  INFO] pringframework.web.context.ContextLoader: 276 [localhost-startStop-1] - Root WebApplicationContext: initialization completed in 1158 ms
[2017-05-04 14:18:16,813  INFO] boot.web.servlet.ServletRegistrationBean: 190 [localhost-startStop-1] - Mapping servlet: 'dispatcherServlet' to [/]
[2017-05-04 14:18:16,818  INFO] .boot.web.servlet.FilterRegistrationBean: 258 [localhost-startStop-1] - Mapping filter: 'metricsFilter' to: [/*]
[2017-05-04 14:18:16,819  INFO] .boot.web.servlet.FilterRegistrationBean: 258 [localhost-startStop-1] - Mapping filter: 'characterEncodingFilter' to: [/*]
[2017-05-04 14:18:16,819  INFO] .boot.web.servlet.FilterRegistrationBean: 258 [localhost-startStop-1] - Mapping filter: 'hiddenHttpMethodFilter' to: [/*]
[2017-05-04 14:18:16,819  INFO] .boot.web.servlet.FilterRegistrationBean: 258 [localhost-startStop-1] - Mapping filter: 'httpPutFormContentFilter' to: [/*]
[2017-05-04 14:18:16,820  INFO] .boot.web.servlet.FilterRegistrationBean: 258 [localhost-startStop-1] - Mapping filter: 'requestContextFilter' to: [/*]
[2017-05-04 14:18:16,821  INFO] .boot.web.servlet.FilterRegistrationBean: 258 [localhost-startStop-1] - Mapping filter: 'webRequestLoggingFilter' to: [/*]
[2017-05-04 14:18:16,821  INFO] .boot.web.servlet.FilterRegistrationBean: 258 [localhost-startStop-1] - Mapping filter: 'applicationContextIdFilter' to: [/*]
[2017-05-04 14:18:17,157  INFO] .annotation.RequestMappingHandlerAdapter: 534 [main    ] - Looking for @ControllerAdvice: org.springframework.boot.context.embedded.AnnotationConfigEmbeddedWebApplicationContext@52aa2946: startup date [Thu May 04 14:18:15 CEST 2017]; root of context hierarchy
[2017-05-04 14:18:17,224  INFO] .annotation.RequestMappingHandlerMapping: 543 [main    ] - Mapped "{[/error],produces=[text/html]}" onto public org.springframework.web.servlet.ModelAndView org.springframework.boot.autoconfigure.web.BasicErrorController.errorHtml(javax.servlet.http.HttpServletRequest,javax.servlet.http.HttpServletResponse)
[2017-05-04 14:18:17,225  INFO] .annotation.RequestMappingHandlerMapping: 543 [main    ] - Mapped "{[/error]}" onto public org.springframework.http.ResponseEntity<java.util.Map<java.lang.String, java.lang.Object>> org.springframework.boot.autoconfigure.web.BasicErrorController.error(javax.servlet.http.HttpServletRequest)
[2017-05-04 14:18:17,245  INFO] .servlet.handler.SimpleUrlHandlerMapping: 362 [main    ] - Mapped URL path [/webjars/**] onto handler of type [class org.springframework.web.servlet.resource.ResourceHttpRequestHandler]
[2017-05-04 14:18:17,245  INFO] .servlet.handler.SimpleUrlHandlerMapping: 362 [main    ] - Mapped URL path [/**] onto handler of type [class org.springframework.web.servlet.resource.ResourceHttpRequestHandler]
[2017-05-04 14:18:17,273  INFO] .servlet.handler.SimpleUrlHandlerMapping: 362 [main    ] - Mapped URL path [/**/favicon.ico] onto handler of type [class org.springframework.web.servlet.resource.ResourceHttpRequestHandler]
[2017-05-04 14:18:17,468  INFO] uate.endpoint.mvc.EndpointHandlerMapping: 543 [main    ] - Mapped "{[/metrics/{name:.*}],methods=[GET],produces=[application/vnd.spring-boot.actuator.v1+json || application/json]}" onto public java.lang.Object org.springframework.boot.actuate.endpoint.mvc.MetricsMvcEndpoint.value(java.lang.String)
[2017-05-04 14:18:17,469  INFO] uate.endpoint.mvc.EndpointHandlerMapping: 543 [main    ] - Mapped "{[/metrics || /metrics.json],methods=[GET],produces=[application/vnd.spring-boot.actuator.v1+json || application/json]}" onto public java.lang.Object org.springframework.boot.actuate.endpoint.mvc.EndpointMvcAdapter.invoke()
[2017-05-04 14:18:17,469  INFO] uate.endpoint.mvc.EndpointHandlerMapping: 543 [main    ] - Mapped "{[/configprops || /configprops.json],methods=[GET],produces=[application/vnd.spring-boot.actuator.v1+json || application/json]}" onto public java.lang.Object org.springframework.boot.actuate.endpoint.mvc.EndpointMvcAdapter.invoke()
[2017-05-04 14:18:17,470  INFO] uate.endpoint.mvc.EndpointHandlerMapping: 543 [main    ] - Mapped "{[/dump || /dump.json],methods=[GET],produces=[application/vnd.spring-boot.actuator.v1+json || application/json]}" onto public java.lang.Object org.springframework.boot.actuate.endpoint.mvc.EndpointMvcAdapter.invoke()
[2017-05-04 14:18:17,472  INFO] uate.endpoint.mvc.EndpointHandlerMapping: 543 [main    ] - Mapped "{[/loggers/{name:.*}],methods=[GET],produces=[application/vnd.spring-boot.actuator.v1+json || application/json]}" onto public java.lang.Object org.springframework.boot.actuate.endpoint.mvc.LoggersMvcEndpoint.get(java.lang.String)
[2017-05-04 14:18:17,472  INFO] uate.endpoint.mvc.EndpointHandlerMapping: 543 [main    ] - Mapped "{[/loggers/{name:.*}],methods=[POST],consumes=[application/vnd.spring-boot.actuator.v1+json || application/json],produces=[application/vnd.spring-boot.actuator.v1+json || application/json]}" onto public java.lang.Object org.springframework.boot.actuate.endpoint.mvc.LoggersMvcEndpoint.set(java.lang.String,java.util.Map<java.lang.String, java.lang.String>)
[2017-05-04 14:18:17,472  INFO] uate.endpoint.mvc.EndpointHandlerMapping: 543 [main    ] - Mapped "{[/loggers || /loggers.json],methods=[GET],produces=[application/vnd.spring-boot.actuator.v1+json || application/json]}" onto public java.lang.Object org.springframework.boot.actuate.endpoint.mvc.EndpointMvcAdapter.invoke()
[2017-05-04 14:18:17,473  INFO] uate.endpoint.mvc.EndpointHandlerMapping: 543 [main    ] - Mapped "{[/heapdump || /heapdump.json],methods=[GET],produces=[application/octet-stream]}" onto public void org.springframework.boot.actuate.endpoint.mvc.HeapdumpMvcEndpoint.invoke(boolean,javax.servlet.http.HttpServletRequest,javax.servlet.http.HttpServletResponse) throws java.io.IOException,javax.servlet.ServletException
[2017-05-04 14:18:17,474  INFO] uate.endpoint.mvc.EndpointHandlerMapping: 543 [main    ] - Mapped "{[/health || /health.json],methods=[GET],produces=[application/vnd.spring-boot.actuator.v1+json || application/json]}" onto public java.lang.Object org.springframework.boot.actuate.endpoint.mvc.HealthMvcEndpoint.invoke(javax.servlet.http.HttpServletRequest,java.security.Principal)
[2017-05-04 14:18:17,476  INFO] uate.endpoint.mvc.EndpointHandlerMapping: 543 [main    ] - Mapped "{[/mappings || /mappings.json],methods=[GET],produces=[application/vnd.spring-boot.actuator.v1+json || application/json]}" onto public java.lang.Object org.springframework.boot.actuate.endpoint.mvc.EndpointMvcAdapter.invoke()
[2017-05-04 14:18:17,477  INFO] uate.endpoint.mvc.EndpointHandlerMapping: 543 [main    ] - Mapped "{[/env/{name:.*}],methods=[GET],produces=[application/vnd.spring-boot.actuator.v1+json || application/json]}" onto public java.lang.Object org.springframework.boot.actuate.endpoint.mvc.EnvironmentMvcEndpoint.value(java.lang.String)
[2017-05-04 14:18:17,477  INFO] uate.endpoint.mvc.EndpointHandlerMapping: 543 [main    ] - Mapped "{[/env || /env.json],methods=[GET],produces=[application/vnd.spring-boot.actuator.v1+json || application/json]}" onto public java.lang.Object org.springframework.boot.actuate.endpoint.mvc.EndpointMvcAdapter.invoke()
[2017-05-04 14:18:17,477  INFO] uate.endpoint.mvc.EndpointHandlerMapping: 543 [main    ] - Mapped "{[/trace || /trace.json],methods=[GET],produces=[application/vnd.spring-boot.actuator.v1+json || application/json]}" onto public java.lang.Object org.springframework.boot.actuate.endpoint.mvc.EndpointMvcAdapter.invoke()
[2017-05-04 14:18:17,478  INFO] uate.endpoint.mvc.EndpointHandlerMapping: 543 [main    ] - Mapped "{[/info || /info.json],methods=[GET],produces=[application/vnd.spring-boot.actuator.v1+json || application/json]}" onto public java.lang.Object org.springframework.boot.actuate.endpoint.mvc.EndpointMvcAdapter.invoke()
[2017-05-04 14:18:17,479  INFO] uate.endpoint.mvc.EndpointHandlerMapping: 543 [main    ] - Mapped "{[/beans || /beans.json],methods=[GET],produces=[application/vnd.spring-boot.actuator.v1+json || application/json]}" onto public java.lang.Object org.springframework.boot.actuate.endpoint.mvc.EndpointMvcAdapter.invoke()
[2017-05-04 14:18:17,479  INFO] uate.endpoint.mvc.EndpointHandlerMapping: 543 [main    ] - Mapped "{[/auditevents || /auditevents.json],methods=[GET],produces=[application/vnd.spring-boot.actuator.v1+json || application/json]}" onto public org.springframework.http.ResponseEntity<?> org.springframework.boot.actuate.endpoint.mvc.AuditEventsMvcEndpoint.findByPrincipalAndAfterAndType(java.lang.String,java.util.Date,java.lang.String)
[2017-05-04 14:18:17,480  INFO] uate.endpoint.mvc.EndpointHandlerMapping: 543 [main    ] - Mapped "{[/autoconfig || /autoconfig.json],methods=[GET],produces=[application/vnd.spring-boot.actuator.v1+json || application/json]}" onto public java.lang.Object org.springframework.boot.actuate.endpoint.mvc.EndpointMvcAdapter.invoke()
[2017-05-04 14:18:17,555  INFO] xport.annotation.AnnotationMBeanExporter: 431 [main    ] - Registering beans for JMX exposure on startup
[2017-05-04 14:18:17,558  INFO] tuate.endpoint.jmx.EndpointMBeanExporter: 431 [main    ] - Registering beans for JMX exposure on startup
[2017-05-04 14:18:17,562  INFO] ontext.support.DefaultLifecycleProcessor: 343 [main    ] - Starting beans in phase 0
[2017-05-04 14:18:17,564  INFO] tuate.endpoint.jmx.EndpointMBeanExporter: 678 [main    ] - Located managed bean 'auditEventsEndpoint': registering with JMX server as MBean [org.springframework.boot:type=Endpoint,name=auditEventsEndpoint]
[2017-05-04 14:18:17,576  INFO] tuate.endpoint.jmx.EndpointMBeanExporter: 678 [main    ] - Located managed bean 'requestMappingEndpoint': registering with JMX server as MBean [org.springframework.boot:type=Endpoint,name=requestMappingEndpoint]
[2017-05-04 14:18:17,583  INFO] tuate.endpoint.jmx.EndpointMBeanExporter: 678 [main    ] - Located managed bean 'environmentEndpoint': registering with JMX server as MBean [org.springframework.boot:type=Endpoint,name=environmentEndpoint]
[2017-05-04 14:18:17,587  INFO] tuate.endpoint.jmx.EndpointMBeanExporter: 678 [main    ] - Located managed bean 'healthEndpoint': registering with JMX server as MBean [org.springframework.boot:type=Endpoint,name=healthEndpoint]
[2017-05-04 14:18:17,589  INFO] tuate.endpoint.jmx.EndpointMBeanExporter: 678 [main    ] - Located managed bean 'beansEndpoint': registering with JMX server as MBean [org.springframework.boot:type=Endpoint,name=beansEndpoint]
[2017-05-04 14:18:17,592  INFO] tuate.endpoint.jmx.EndpointMBeanExporter: 678 [main    ] - Located managed bean 'infoEndpoint': registering with JMX server as MBean [org.springframework.boot:type=Endpoint,name=infoEndpoint]
[2017-05-04 14:18:17,594  INFO] tuate.endpoint.jmx.EndpointMBeanExporter: 678 [main    ] - Located managed bean 'loggersEndpoint': registering with JMX server as MBean [org.springframework.boot:type=Endpoint,name=loggersEndpoint]
[2017-05-04 14:18:17,599  INFO] tuate.endpoint.jmx.EndpointMBeanExporter: 678 [main    ] - Located managed bean 'metricsEndpoint': registering with JMX server as MBean [org.springframework.boot:type=Endpoint,name=metricsEndpoint]
[2017-05-04 14:18:17,601  INFO] tuate.endpoint.jmx.EndpointMBeanExporter: 678 [main    ] - Located managed bean 'traceEndpoint': registering with JMX server as MBean [org.springframework.boot:type=Endpoint,name=traceEndpoint]
[2017-05-04 14:18:17,603  INFO] tuate.endpoint.jmx.EndpointMBeanExporter: 678 [main    ] - Located managed bean 'dumpEndpoint': registering with JMX server as MBean [org.springframework.boot:type=Endpoint,name=dumpEndpoint]
[2017-05-04 14:18:17,605  INFO] tuate.endpoint.jmx.EndpointMBeanExporter: 678 [main    ] - Located managed bean 'autoConfigurationReportEndpoint': registering with JMX server as MBean [org.springframework.boot:type=Endpoint,name=autoConfigurationReportEndpoint]
[2017-05-04 14:18:17,608  INFO] tuate.endpoint.jmx.EndpointMBeanExporter: 678 [main    ] - Located managed bean 'configurationPropertiesReportEndpoint': registering with JMX server as MBean [org.springframework.boot:type=Endpoint,name=configurationPropertiesReportEndpoint]
[2017-05-04 14:18:17,626  INFO] g.apache.coyote.http11.Http11NioProtocol: 179 [main    ] - Initializing ProtocolHandler ["http-nio-8080"]
[2017-05-04 14:18:17,634  INFO] g.apache.coyote.http11.Http11NioProtocol: 179 [main    ] - Starting ProtocolHandler ["http-nio-8080"]
[2017-05-04 14:18:17,647  INFO] g.apache.tomcat.util.net.NioSelectorPool: 179 [main    ] - Using a shared selector for servlet write/read
[2017-05-04 14:18:17,660  INFO] ed.tomcat.TomcatEmbeddedServletContainer: 198 [main    ] - Tomcat started on port(s): 8080 (http)
[2017-05-04 14:18:17,668  INFO] ver.frontend.impl.springboot.Application:  57 [main    ] - Started Application in 2.648 seconds (JVM running for 2.979)
```
So the server is running, but you haven’t defined any business endpoints yet.

