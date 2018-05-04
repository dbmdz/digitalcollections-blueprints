# DigitalCollections Blueprints: Webapp (Spring Boot + Thymeleaf)

This project provides a production ready Webapp skeleton based on Spring Boot and Thymeleaf.

See also the [Spring Boot Reference Guide](http://docs.spring.io/spring-boot/docs/current/reference/html/index.html).

Features documentation:

- [doc/README-01-Initial_setup_packaging_running.md](doc/README-01-Initial_setup_packaging_running.md): Initial Setup, Build WAR/JAR, Run webapp
- [doc/README-02-Spring_Actuator.md](doc/README-02-Spring_Actuator.md): Spring Actuator: basic endpoints, HAL browser, management port
- [doc/README-03-Application_information.md](doc/README-03-Application_information.md): Application information, project encoding
- [doc/README-04-Application_configuration.md](doc/README-04-Application_configuration.md): Application configuration (including environment specific configuration)
- [doc/README-05-Controller_endpoint.md](doc/README-05-Controller_endpoint.md): Controller endpoint for displaying a thymeleaf page.
- [doc/README-06-Security_configuration.md](doc/README-06-Security_configuration.md): Security configuration (unsecured webpage, secured actuator).
- [doc/README-07-Logging.md](doc/README-07-Logging.md): Logging (Logback)
- [doc/README-08-Unit_testing.md](doc/README-08-Unit_testing.md): Unit-Testing
- [doc/README-09-Monitoring.md](doc/README-09-Monitoring.md): Monitoring

## Usage

- Unsecured Webpage: <http://localhost:9000/>
- Secured Actuator endpoint (HAL Browser): <http://localhost:9001/monitoring>
- (Un)Secured Actuator health-endpoint: <http://localhost:9001/monitoring/health>

## Migration notes from Spring Boot 1.5.8 to Spring Boot 2.0.1

see
- <https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-2.0-Migration-Guide>
- <https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-Security-2.0>
- <http://www.baeldung.com/spring-boot-actuators#boot-2x-actuator>
- <https://ultraq.github.io/thymeleaf-layout-dialect/MigrationGuide.html>

### pom.xml

Add temporarily Migrator-dependency:

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-properties-migrator</artifactId>
  <scope>runtime</scope>
</dependency>
```

Upgrade Logback-Logstash-Encoder to version 5.1:

```xml
<properties>
  <version.logstash-logback-encoder>5.1</version.logstash-logback-encoder>
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

Management endpoints:

Unlike in previous versions, Actuator comes with most endpoints disabled. Thus, the only two available by default are /health and /info. Would we want to enable all of them, we could set management.endpoints.web.exposure.include="*". Alternatively, we could list endpoints which should be enabled.

### Unit tests

Rename test properties:

```java
@TestPropertySource(properties = {"management.port=0" ...}) -> @TestPropertySource(properties = {"management.server.port=0" ...})
```

Change:

```java
@Value("${local.management.port}") -> @LocalManagementPort
@Value("${local.server.port}") -> @LocalServerPort
```

Remove test properties:

```java
@TestPropertySource(properties = {..., "management.security.enabled=true"})
```

### Spring Security

"Spring Boot 2.0 does not provide separate auto-configuration for user-defined endpoints and actuator endpoints. When Spring Security is on the classpath, the auto-configuration secures all endpoints by default."

Nice. Problem: ALL endpoints are secured, even all your webapp controller endpoints and static resources (css, ...)...

Disabling security for user defined endpoints but not for actuator endpoints (except info and health):

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
    http
            .authorizeRequests()
            .requestMatchers(EndpointRequest.to("info", "health")).permitAll()
            .requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole("ACTUATOR")
            .antMatchers("/**").permitAll()
            .and()
            .httpBasic();
  }

}
```

If you have also setup security for your webapp controllers, you have to explicitely allow access to static resources:

```java
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;

@Configuration
public class SpringConfigSecurityWebapp extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests().requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll();
    http.authorizeRequests().antMatchers("/api/**", "/setup/**").permitAll();
```

### Thymeleaf

"The Thymeleaf starter included the thymeleaf-layout-dialect dependency previously. Since Thymeleaf 3.0 now offers a [native way to implement layouts](https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#template-layout), we removed that mandatory dependency and leave this choice up to you. If your application is relying on the layout-dialect project, please add it explicitly as a dependency."

1. Remove all explicitely defined thymeleaf-dependencies from pom.xml.
2. Add Thymeleaf-layout-dialect without version (is managed):

```xml
<dependency>
  <groupId>nz.net.ultraq.thymeleaf</groupId>
  <artifactId>thymeleaf-layout-dialect</artifactId>
</dependency>
```

When you start your application, you will get some warnings about deprecated expressions:

```sh
The layout:decorator/data-layout-decorator processor has been deprecated and will be removed in the next major version of the layout dialect.  Please use layout:decorate/data-layout-decorate instead to future-proof your code.  See https://github.com/ultraq/thymeleaf-layout-dialect/issues/95 for more information.

Fragment expression "base" is being wrapped as a Thymeleaf 3 fragment expression (~{...}) for backwards compatibility purposes.  This wrapping will be dropped in the next major version of the expression processor, so please rewrite as a Thymeleaf 3 fragment expression to future-proof your code.  See https://github.com/thymeleaf/thymeleaf/issues/451 for more information.
```

See <https://ultraq.github.io/thymeleaf-layout-dialect/MigrationGuide.html>

- "decorator processor renamed to decorate" and "Thymeleaf 3 fragment processors":

old:
```html
<html xmlns:th="http://www.thymeleaf.org"
      xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout"
      layout:decorator="base">
```

new:
```html
<html xmlns:th="http://www.thymeleaf.org"
      xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout"
      layout:decorate="~{base}">
```

- "$DECORATOR_TITLE renamed to $LAYOUT_TITLE": $DECORATOR_TITLE -> $LAYOUT_TITLE
- "Deprecated include, introduced insert": th:include -> th:insert