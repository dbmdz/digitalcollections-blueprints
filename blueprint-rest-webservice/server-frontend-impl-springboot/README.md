# DigitalCollections Blueprints: Spring Boot REST Webservice Server

This project provides a production ready RESTful Web Service skeleton based on Spring Boot.
It started from this guide: <https://spring.io/guides/gs/actuator-service/>.

See also the [Spring Boot Reference Guide](http://docs.spring.io/spring-boot/docs/current/reference/html/index.html).

Features documentation:

- [doc/README-01.md](doc/README-01.md): Initial Setup, Build WAR/JAR, Run webservice
- [doc/README-02.md](doc/README-02.md): Spring Actuator: basic endpoints, HAL browser, management port
- [doc/README-03.md](doc/README-03.md): Application information, project encoding
- [doc/README-04.md](doc/README-04.md): Application configuration (1)
- [doc/README-05.md](doc/README-05.md): REST-API: Model, Controller, Request/Response
- [doc/README-06.md](doc/README-06.md): Logging (Logback)
- [doc/README-07.md](doc/README-07.md): Unit-Testing
- [doc/README-08.md](doc/README-08.md): Migrating from @Controller to @RestController
- [doc/README-09.md](doc/README-09.md): JSONDoc GUI and API documentation
- [doc/README-10.md](doc/README-10.md): Application configuration (2): Environment specific configuration

## Usage

### Greeting endpoint

- <http://localhost:9000/hello>

Response:

```json
{
  id: 2,
  content: "Hello, Stranger!"
}
```

- <http://localhost:9000/hello?name=Sepp>

Response:

```json
{
  id: 3,
  content: "Hello, Sepp!"
}
```

### Actuator endpoint (HAL Browser)

<http://localhost:9001/actuator>

### JSONDoc endpoint (JSONDoc UI)

<http://localhost:9000/jsondoc-ui.html> with JSONDoc URL <http://localhost:9000/jsondoc>