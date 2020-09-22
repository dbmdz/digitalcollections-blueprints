# Spring Actuator

There aren’t any endpoints defined in this application, but there’s enough to launch things and see some of Spring Boot's default and Actuator’s features. 

```sh
$ curl localhost:8080
{"timestamp":1493900533248,"status":404,"error":"Not Found","message":"No message available","path":"/"}
```

Instead of a default container-generated HTML error response, you see a generic JSON response from the `/error` endpoint. You can see in the console logs from the server startup which endpoints are provided out of the box.

see <https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-endpoints.html>

Spring Boot includes a number of additional features to help you monitor and manage your application when it’s pushed to production. You can choose to manage and monitor your application using HTTP endpoints, with JMX or even by remote shell (SSH or Telnet). Auditing, health and metrics gathering can be automatically applied to your application.

The way that endpoints are exposed will depend on the type of technology that you choose. Most applications choose HTTP monitoring, where the ID of the endpoint is mapped to a URL. For example, by default, the health endpoint will be mapped to `/health`.

## Basic endpoints

By default, all endpoints except `shutdown` are enabled. Individual endpoints can be enabled or disabled by configuring
their `management.endpoint.<id>.enabled` property. 
The following example enables the `shutdown` endpoint:

```yml
management.endpoint.shutdown.enabled=true
```

Since Endpoints may contain sensitive information, careful consideration should be given about when
to expose them. To change which endpoints are exposed (remotely accessible via JMX or HTTP), use the following technology-specific
`include` and `exclude` properties. `*` can be used to select all endpoints. For example, to expose
everything over HTTP except the env and beans endpoints, use the following properties:

```yml
management.endpoints.web.exposure.include=*
management.endpoints.web.exposure.exclude=env,beans
```

To enable all available endpoints (don't do this unsecured in production!), we need to configure this.
Configuration of a Spring Boot application is done in classpath resource `application.yml`:

File `src/main/resources/application.yml`:

```yml
management:
  endpoints:
    web:
      exposure:
        include: "*"
```

After restart we see a lot more endpoints in log output:

```
2018-09-21 08:41:19.150  INFO 29753 --- [           main] o.s.b.a.e.web.EndpointLinksResolver      : Exposing 14 endpoint(s) beneath base path '/actuator'
2018-09-21 08:41:19.157  INFO 29753 --- [           main] s.b.a.e.w.s.WebMvcEndpointHandlerMapping : Mapped "{[/actuator/auditevents],methods=[GET],produces=[application/vnd.spring-boot.actuator.v2+json || application/json]}" onto public java.lang.Object org.springframework.boot.actuate.endpoint.web.servlet.AbstractWebMvcEndpointHandlerMapping$OperationHandler.handle(javax.servlet.http.HttpServletRequest,java.util.Map<java.lang.String, java.lang.String>)
2018-09-21 08:41:19.157  INFO 29753 --- [           main] s.b.a.e.w.s.WebMvcEndpointHandlerMapping : Mapped "{[/actuator/beans],methods=[GET],produces=[application/vnd.spring-boot.actuator.v2+json || application/json]}" onto public java.lang.Object org.springframework.boot.actuate.endpoint.web.servlet.AbstractWebMvcEndpointHandlerMapping$OperationHandler.handle(javax.servlet.http.HttpServletRequest,java.util.Map<java.lang.String, java.lang.String>)
2018-09-21 08:41:19.158  INFO 29753 --- [           main] s.b.a.e.w.s.WebMvcEndpointHandlerMapping : Mapped "{[/actuator/health],methods=[GET],produces=[application/vnd.spring-boot.actuator.v2+json || application/json]}" onto public java.lang.Object org.springframework.boot.actuate.endpoint.web.servlet.AbstractWebMvcEndpointHandlerMapping$OperationHandler.handle(javax.servlet.http.HttpServletRequest,java.util.Map<java.lang.String, java.lang.String>)
2018-09-21 08:41:19.158  INFO 29753 --- [           main] s.b.a.e.w.s.WebMvcEndpointHandlerMapping : Mapped "{[/actuator/conditions],methods=[GET],produces=[application/vnd.spring-boot.actuator.v2+json || application/json]}" onto public java.lang.Object org.springframework.boot.actuate.endpoint.web.servlet.AbstractWebMvcEndpointHandlerMapping$OperationHandler.handle(javax.servlet.http.HttpServletRequest,java.util.Map<java.lang.String, java.lang.String>)
2018-09-21 08:41:19.158  INFO 29753 --- [           main] s.b.a.e.w.s.WebMvcEndpointHandlerMapping : Mapped "{[/actuator/configprops],methods=[GET],produces=[application/vnd.spring-boot.actuator.v2+json || application/json]}" onto public java.lang.Object org.springframework.boot.actuate.endpoint.web.servlet.AbstractWebMvcEndpointHandlerMapping$OperationHandler.handle(javax.servlet.http.HttpServletRequest,java.util.Map<java.lang.String, java.lang.String>)
2018-09-21 08:41:19.158  INFO 29753 --- [           main] s.b.a.e.w.s.WebMvcEndpointHandlerMapping : Mapped "{[/actuator/env],methods=[GET],produces=[application/vnd.spring-boot.actuator.v2+json || application/json]}" onto public java.lang.Object org.springframework.boot.actuate.endpoint.web.servlet.AbstractWebMvcEndpointHandlerMapping$OperationHandler.handle(javax.servlet.http.HttpServletRequest,java.util.Map<java.lang.String, java.lang.String>)
2018-09-21 08:41:19.159  INFO 29753 --- [           main] s.b.a.e.w.s.WebMvcEndpointHandlerMapping : Mapped "{[/actuator/env/{toMatch}],methods=[GET],produces=[application/vnd.spring-boot.actuator.v2+json || application/json]}" onto public java.lang.Object org.springframework.boot.actuate.endpoint.web.servlet.AbstractWebMvcEndpointHandlerMapping$OperationHandler.handle(javax.servlet.http.HttpServletRequest,java.util.Map<java.lang.String, java.lang.String>)
2018-09-21 08:41:19.159  INFO 29753 --- [           main] s.b.a.e.w.s.WebMvcEndpointHandlerMapping : Mapped "{[/actuator/info],methods=[GET],produces=[application/vnd.spring-boot.actuator.v2+json || application/json]}" onto public java.lang.Object org.springframework.boot.actuate.endpoint.web.servlet.AbstractWebMvcEndpointHandlerMapping$OperationHandler.handle(javax.servlet.http.HttpServletRequest,java.util.Map<java.lang.String, java.lang.String>)
2018-09-21 08:41:19.159  INFO 29753 --- [           main] s.b.a.e.w.s.WebMvcEndpointHandlerMapping : Mapped "{[/actuator/loggers],methods=[GET],produces=[application/vnd.spring-boot.actuator.v2+json || application/json]}" onto public java.lang.Object org.springframework.boot.actuate.endpoint.web.servlet.AbstractWebMvcEndpointHandlerMapping$OperationHandler.handle(javax.servlet.http.HttpServletRequest,java.util.Map<java.lang.String, java.lang.String>)
2018-09-21 08:41:19.159  INFO 29753 --- [           main] s.b.a.e.w.s.WebMvcEndpointHandlerMapping : Mapped "{[/actuator/loggers/{name}],methods=[GET],produces=[application/vnd.spring-boot.actuator.v2+json || application/json]}" onto public java.lang.Object org.springframework.boot.actuate.endpoint.web.servlet.AbstractWebMvcEndpointHandlerMapping$OperationHandler.handle(javax.servlet.http.HttpServletRequest,java.util.Map<java.lang.String, java.lang.String>)
2018-09-21 08:41:19.160  INFO 29753 --- [           main] s.b.a.e.w.s.WebMvcEndpointHandlerMapping : Mapped "{[/actuator/loggers/{name}],methods=[POST],consumes=[application/vnd.spring-boot.actuator.v2+json || application/json]}" onto public java.lang.Object org.springframework.boot.actuate.endpoint.web.servlet.AbstractWebMvcEndpointHandlerMapping$OperationHandler.handle(javax.servlet.http.HttpServletRequest,java.util.Map<java.lang.String, java.lang.String>)
2018-09-21 08:41:19.160  INFO 29753 --- [           main] s.b.a.e.w.s.WebMvcEndpointHandlerMapping : Mapped "{[/actuator/heapdump],methods=[GET],produces=[application/octet-stream]}" onto public java.lang.Object org.springframework.boot.actuate.endpoint.web.servlet.AbstractWebMvcEndpointHandlerMapping$OperationHandler.handle(javax.servlet.http.HttpServletRequest,java.util.Map<java.lang.String, java.lang.String>)
2018-09-21 08:41:19.161  INFO 29753 --- [           main] s.b.a.e.w.s.WebMvcEndpointHandlerMapping : Mapped "{[/actuator/threaddump],methods=[GET],produces=[application/vnd.spring-boot.actuator.v2+json || application/json]}" onto public java.lang.Object org.springframework.boot.actuate.endpoint.web.servlet.AbstractWebMvcEndpointHandlerMapping$OperationHandler.handle(javax.servlet.http.HttpServletRequest,java.util.Map<java.lang.String, java.lang.String>)
2018-09-21 08:41:19.161  INFO 29753 --- [           main] s.b.a.e.w.s.WebMvcEndpointHandlerMapping : Mapped "{[/actuator/metrics/{requiredMetricName}],methods=[GET],produces=[application/vnd.spring-boot.actuator.v2+json || application/json]}" onto public java.lang.Object org.springframework.boot.actuate.endpoint.web.servlet.AbstractWebMvcEndpointHandlerMapping$OperationHandler.handle(javax.servlet.http.HttpServletRequest,java.util.Map<java.lang.String, java.lang.String>)
2018-09-21 08:41:19.161  INFO 29753 --- [           main] s.b.a.e.w.s.WebMvcEndpointHandlerMapping : Mapped "{[/actuator/metrics],methods=[GET],produces=[application/vnd.spring-boot.actuator.v2+json || application/json]}" onto public java.lang.Object org.springframework.boot.actuate.endpoint.web.servlet.AbstractWebMvcEndpointHandlerMapping$OperationHandler.handle(javax.servlet.http.HttpServletRequest,java.util.Map<java.lang.String, java.lang.String>)
2018-09-21 08:41:19.161  INFO 29753 --- [           main] s.b.a.e.w.s.WebMvcEndpointHandlerMapping : Mapped "{[/actuator/scheduledtasks],methods=[GET],produces=[application/vnd.spring-boot.actuator.v2+json || application/json]}" onto public java.lang.Object org.springframework.boot.actuate.endpoint.web.servlet.AbstractWebMvcEndpointHandlerMapping$OperationHandler.handle(javax.servlet.http.HttpServletRequest,java.util.Map<java.lang.String, java.lang.String>)
2018-09-21 08:41:19.161  INFO 29753 --- [           main] s.b.a.e.w.s.WebMvcEndpointHandlerMapping : Mapped "{[/actuator/httptrace],methods=[GET],produces=[application/vnd.spring-boot.actuator.v2+json || application/json]}" onto public java.lang.Object org.springframework.boot.actuate.endpoint.web.servlet.AbstractWebMvcEndpointHandlerMapping$OperationHandler.handle(javax.servlet.http.HttpServletRequest,java.util.Map<java.lang.String, java.lang.String>)
2018-09-21 08:41:19.162  INFO 29753 --- [           main] s.b.a.e.w.s.WebMvcEndpointHandlerMapping : Mapped "{[/actuator/mappings],methods=[GET],produces=[application/vnd.spring-boot.actuator.v2+json || application/json]}" onto public java.lang.Object org.springframework.boot.actuate.endpoint.web.servlet.AbstractWebMvcEndpointHandlerMapping$OperationHandler.handle(javax.servlet.http.HttpServletRequest,java.util.Map<java.lang.String, java.lang.String>)
2018-09-21 08:41:19.162  INFO 29753 --- [           main] s.b.a.e.w.s.WebMvcEndpointHandlerMapping : Mapped "{[/actuator],methods=[GET],produces=[application/vnd.spring-boot.actuator.v2+json || application/json]}" onto protected java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.springframework.boot.actuate.endpoint.web.Link>> org.springframework.boot.actuate.endpoint.web.servlet.WebMvcEndpointHandlerMapping.links(javax.servlet.http.HttpServletRequest,javax.servlet.http.HttpServletResponse)
```

Here is a complete list of all endpoints:

| Endpoint    |	Description | Sensitive Default |
|-------------|-------------|-------------------|
| /actuator   | Provides a hypermedia-based “discovery page” for the other endpoints. | true
| /actuator/auditevents| Exposes audit events information for the current application. | true
| /actuator/autoconfig | Displays an auto-configuration report showing all auto-configuration candidates and the reason why they ‘were’ or ‘were not’ applied. | true
| /actuator/beans      | Displays a complete list of all the Spring beans in your application. | true
| /actuator/configprops| Displays a collated list of all @ConfigurationProperties. | true
| /actuator/dump       | Performs a thread dump. | true
| /actuator/env        | Exposes properties from Spring’s ConfigurableEnvironment. | true
| /actuator/flyway     | Shows any Flyway database migrations that have been applied. | true
| /actuator/health     | Shows application health information (when the application is secure, a simple ‘status’ when accessed over an unauthenticated connection or full message details when authenticated). | false
| /actuator/info       | Displays arbitrary application info. | false
| /actuator/liquibase  | Shows any Liquibase database migrations that have been applied. | true
| /actuator/loggers    | Shows and modifies the configuration of loggers in the application. | true
| /actuator/metrics    | Shows ‘metrics’ information for the current application. | true
| /actuator/mappings   | Displays a collated list of all @RequestMapping paths. | true
| /actuator/shutdown   | Allows the application to be gracefully shutdown (not enabled by default). | true
| /actuator/trace      | Displays trace information (by default the last 100 HTTP requests). | true

Depending on how an endpoint is exposed, the sensitive property may be used as a security hint. For example, sensitive endpoints will require a username/password when they are accessed over HTTP (or simply disabled if web security is not enabled), otherwise you get a "401: Unauthorized" if not authenticated.

If you are using Spring MVC, the following additional endpoints can also be used:

| Endpoint    |	Description | Sensitive Default |
|-------------|-------------|-------------------|
| /actuator/docs       | Displays documentation, including example requests and responses, for the Actuator’s endpoints. Requires spring-boot-actuator-docs to be on the classpath. | false
| /actuator/heapdump   | Returns a GZip compressed hprof heap dump file. | true
| /actuator/jolokia    | Exposes JMX beans over HTTP (when Jolokia is on the classpath). | true
| /actuator/logfile    | Returns the contents of the logfile (if logging.file or logging.path properties have been set). Supports the use of the HTTP Range header to retrieve part of the log file’s content. | true

By default all sensitive HTTP endpoints are secured such that only users that have an `ACTUATOR` role may access them. Security is enforced using the standard `HttpServletRequest.isUserInRole` method.

To get a clickable list of all available endpoints, browse <http://localhost:8080/actuator>

More about actuator at <https://docs.spring.io/spring-boot/docs/2.0.x/actuator-api/html/>

### Secure actuator endpoints

If you’re deploying applications publicly, you may want to add ‘Spring Security’ to handle user authentication. When ‘Spring Security’ is added, by default ‘basic’ authentication will be used with the username `user` and a generated password (which is printed on the console when the application starts).

See <https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-endpoints.html#production-ready-endpoints-security>

Adding Spring Security to dependencies:

File `pom.xml`:

```xml
<dependencies>
  ...
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
  </dependency>
```

You can use Spring application config to change the username and password and to change the security role(s) required to access the endpoints (default role for accessing is `ACTUATOR`). For example, you might set the following in your `application.properties`:

In `src/main/resources/application.yml`:

```yml
spring:
  security:
    user:
      name: admin
      password: secret
      roles: ACTUATOR
```

If you wish to configure custom security for HTTP endpoints, for example, only allow users with role `ENDPOINT_ADMIN`
to access them, Spring Boot provides some convenient RequestMatcher objects that can be used in combination with Spring Security.

A typical Spring Security configuration might look something like the following example:

Add file `src/main/java/de/digitalcollections/blueprints/webapp/springboot/config/SpringConfigSecurity.java`:

```java
package de.digitalcollections.blueprints.webapp.springboot.config;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SpringConfigSecurity extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.requestMatcher(EndpointRequest.toAnyEndpoint()).authorizeRequests()
            .anyRequest().hasRole("ENDPOINT_ADMIN")
            .and()
            .httpBasic();
  }
}
```

Change role of user in `application.yml`:

```yml
...
      roles: ENDPOINT_ADMIN
```

Now all endpoints are secured and you have to login with `admin/secret`to get access to them.

It might be useful to allow unsecured access to some endpoints (e.g. `info` and `health`).
(See <https://docs.spring.io/spring-boot/docs/2.0.5.RELEASE/actuator-api//html/>)

Therefore we allow unauthorized access to them like this:

```java
@Override
protected void configure(HttpSecurity http) throws Exception {
  http.authorizeRequests()
          .requestMatchers(EndpointRequest.to(InfoEndpoint.class, HealthEndpoint.class)).permitAll()
          .requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole("ENDPOINT_ADMIN")
          .and()
          .httpBasic();
}
```

### Testing actuator endpoints

Testing of endpoints can be done with the command line tool `curl` or directly in browser.

Sample `/actuator/health` (with authentication) request and response:

```sh
$ curl http://localhost:8080/actuator/health
{
  "status" : "UP",
}
```

Authentication with basic auth (username/password) is done using `-u`:

```sh
$ curl -u admin:secret http://localhost:8080/actuator/health
{
  "status" : "UP",
}
```

### Default response (list of all actuator endpoints):

When requesting <http://localhost:8080/actuator>:

```json
{
"_links": {
"self": {
"href": "http://localhost:8080/actuator",
"templated": false
},
"auditevents": {
"href": "http://localhost:8080/actuator/auditevents",
"templated": false
},
"beans": {
"href": "http://localhost:8080/actuator/beans",
"templated": false
},
"health": {
"href": "http://localhost:8080/actuator/health",
"templated": false
},
"conditions": {
"href": "http://localhost:8080/actuator/conditions",
"templated": false
},
"configprops": {
"href": "http://localhost:8080/actuator/configprops",
"templated": false
},
"env-toMatch": {
"href": "http://localhost:8080/actuator/env/{toMatch}",
"templated": true
},
"env": {
"href": "http://localhost:8080/actuator/env",
"templated": false
},
"info": {
"href": "http://localhost:8080/actuator/info",
"templated": false
},
"loggers": {
"href": "http://localhost:8080/actuator/loggers",
"templated": false
},
"loggers-name": {
"href": "http://localhost:8080/actuator/loggers/{name}",
"templated": true
},
"heapdump": {
"href": "http://localhost:8080/actuator/heapdump",
"templated": false
},
"threaddump": {
"href": "http://localhost:8080/actuator/threaddump",
"templated": false
},
"metrics-requiredMetricName": {
"href": "http://localhost:8080/actuator/metrics/{requiredMetricName}",
"templated": true
},
"metrics": {
"href": "http://localhost:8080/actuator/metrics",
"templated": false
},
"scheduledtasks": {
"href": "http://localhost:8080/actuator/scheduledtasks",
"templated": false
},
"httptrace": {
"href": "http://localhost:8080/actuator/httptrace",
"templated": false
},
"mappings": {
"href": "http://localhost:8080/actuator/mappings",
"templated": false
}
}
}
```

## Activating detailed health information

See <https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-endpoints.html#production-ready-health>

To get more information about a running application, we can activate detailed informations with `management.endpoint.health.show-details=always` (possible values: "never", "when-authorized", "always"). To get detailed information only for authorized users:

```yml
management:
  endpoint:
    health:
      show-details: when-authorized
  endpoints:
    web:
      exposure:
        include: "*"
```

Now we see valuable information about system state, too:

```sh
$ curl -u admin:secret http://localhost:8080/actuator/health
{
  "status":"UP",
  "details": {
    "diskSpace": {
      "status":"UP",
      "details": {
        "total":120906379264,
        "free":5029457920,
        "threshold":10485760
      }
    }
  }
}
```

You can write additional HealthIndicators for your system state, see <https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-endpoints.html#_writing_custom_healthindicators>.

## Configure management endpoint port

Spring Boot Actuator defaults to run on port 8080 (see <https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-monitoring.html#production-ready-customizing-management-server-port>). If you want to be able to restrict access to endpoint by firewall settings, you need a different port than the webapp/server port. Changing it to e.g. 9001 in `application.yml`:

```yml
management:
  server:
    port: 9001
    ...
```

Sample:

```sh
$ curl -u admin:secret http://localhost:9001/actuator/health
{"status":"UP","diskSpace":{"status":"UP","total":120906379264,"free":18595954688,"threshold":10485760},"_links":{"self":{"href":"http://localhost:9001/health"}
```

For more configuration options see <https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-monitoring.html>.

## Configure management context

By default actuator endpoints are `/actuator`, `/actuator/health` etc. 
Sometimes, it is useful to customize the prefix for the management endpoints.
You can use the management.endpoints.web.base-path property to change the prefix for your management endpoint to e.g. `/monitoring`:

```yml
management:
  endpoints:
    web:
      base-path: '/monitoring'
      exposure:
        include: '*'
    ...
```

Test:

```sh
$ curl -u admin:secret http://localhost:9001/monitoring/health
{"status":"UP","details":{"diskSpace":{"status":"UP","details":{"total":120906379264,"free":5021368320,"threshold":10485760}}}}
```

## Migration Notes

### from Spring Boot 1.5.8 to Spring Boot 2.0.x

see <http://www.baeldung.com/spring-boot-actuators#boot-2x-actuator>:

"Unlike in previous versions, Actuator comes with most endpoints disabled. Thus, the only two available by default are /health and /info. Would we want to enable all of them, we could set management.endpoints.web.exposure.include="*". Alternatively, we could list endpoints which should be enabled."

User must have role `ACTUATOR` to get authorization for actuator endpoints:

```yml
spring:
  security:
    user:
      name: admin
      password: secret
      roles: ACTUATOR
```