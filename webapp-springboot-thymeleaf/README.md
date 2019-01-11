# DigitalCollections Blueprints: Webapp (Spring Boot + Thymeleaf)

This project provides a best practices blueprint for a production ready Webapp skeleton based on Spring Boot and Thymeleaf.

See also the [Spring Boot Reference Guide](http://docs.spring.io/spring-boot/docs/current/reference/html/index.html).

Features documentation (each including Migration notes for version upgrades):

- [doc/README-01-Initial_setup_packaging_running.md](doc/README-01-Initial_setup_packaging_running.md): Initial Setup, Build WAR/JAR, Run webapp
- [doc/README-02-Spring_Actuator.md](doc/README-02-Spring_Actuator.md): Spring Actuator: basic endpoints, HAL browser, management port
- [doc/README-03-Application_information.md](doc/README-03-Application_information.md): Application information, project encoding
- [doc/README-04-Application_configuration.md](doc/README-04-Application_configuration.md): Application configuration (including environment specific configuration)
- [doc/README-05-Controller_endpoint_thymeleaf.md](doc/README-05-Controller_endpoint_thymeleaf.md): Controller endpoint for displaying a thymeleaf page.
- [doc/README-06-Internationalisation.md](doc/README-06-Internationalisation.md): Internationalisation (i18n) ("multilanguage GUI")
- [doc/README-07-Error_handling.md](doc/README-07-Error_handling.md): Error pages and error handling
- [doc/README-08-Logging.md](doc/README-08-Logging.md): Logging (Logback)
- [doc/README-09-Monitoring.md](doc/README-09-Monitoring.md): Monitoring
- [doc/README-10-Security.md](doc/README-10-Security.md): Security
- [doc/README-11-Unit_testing.md](doc/README-11-Unit_testing.md): Unit-Testing
- [doc/README-12-Bootstrap_migration.md](doc/README-12-Bootstrap_migration.md): Migration from Bootstrap 3 to 4

## Usage

- Unsecured Webpage: <http://localhost:9000/>
- Secured Actuator endpoint (HAL Browser): <http://localhost:9001/monitoring>
- (Un)Secured Actuator health-endpoint: <http://localhost:9001/monitoring/health>
