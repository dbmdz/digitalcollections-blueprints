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
