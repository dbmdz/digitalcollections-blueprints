# DigitalCollections Blueprints: Spring Boot REST Webservice Server

This project provides a production ready RESTful Web Service skeleton based on Spring Boot.
It started from this guide: <https://spring.io/guides/gs/actuator-service/>.

See also the [Spring Boot Reference Guide](http://docs.spring.io/spring-boot/docs/current/reference/html/index.html).

Features documentation:

- [doc/README-01-Initial_setup_packaging_running.md](doc/README-01-Initial_setup_packaging_running.md): Initial Setup, Build WAR/JAR, Run webservice
- [doc/README-02-Spring_Actuator.md](doc/README-02-Spring_Actuator.md): Spring Actuator: basic endpoints, HAL browser, management port
- [doc/README-03-Application_information.md](doc/README-03-Application_information.md): Application information, project encoding
- [doc/README-04-Application_configuration.md](doc/README-04-Application_configuration.md): Application configuration (1)
- [doc/README-05-REST_endpoint.md](doc/README-05-REST_endpoint.md): REST-API endpoint: Model, Controller, Request/Response
- [doc/README-06-Logging.md](doc/README-06-Logging.md): Logging (Logback)
- [doc/README-07-Unit_testing.md](doc/README-07-Unit_testing.md): Unit-Testing
- [doc/README-08-Migrating_to_RestController.md](doc/README-08-Migrating_to_RestController.md): Migrating from @Controller to @RestController
- [doc/README-09-JSONDoc.md](doc/README-09-JSONDoc.md): JSONDoc GUI and API documentation
- [doc/README-10-Env_specific_configuration.md](doc/README-10-Env_specific_configuration.md): Application configuration (2): Environment specific configuration
- [doc/README-11-Integration_tests_with_Docker.md](doc/README-11-Integration_tests_with_Docker.md): Integration Tests using Docker as container for REST webservice server
- [doc/README-12-Securing_endpoints.md](doc/README-12-Securing_endpoints.md): Security: add HTTP-Basic authentication to service endpoints
- [doc/README-13-JSONDoc_for_secure_endpoints.md](doc/README-13-JSONDoc_for_secure_endpoints.md): TODO: Add authentication input fields to jsondoc ui

## Usage

### Greeting endpoint

- <http://localhost:9000/hello>

Response:

```json
{
  "id": 2,
  "content": "Hello, Stranger!"
}
```

- <http://localhost:9000/hello?name=Sepp>

Response:

```json
{
  "id": 3,
  "content": "Hello, Sepp!"
}
```

### Actuator endpoint (HAL Browser)

<http://localhost:9001/actuator>

### JSONDoc endpoint (JSONDoc UI)

<http://localhost:9000/jsondoc-ui.html> with JSONDoc URL <http://localhost:9000/jsondoc>

## Run as Docker Container

Build a new Docker image (from the file named `Dockerfile`(convention)) for this service:
  
```bash
$ mvn clean package
$ cd target
$ docker build . -t rest-blueprint
```

Output:

```bash
Sending build context to Docker daemon   19.1MB
Step 1/7 : FROM frolvlad/alpine-oraclejdk8:slim
 ---> 323fb90cf52c
Step 2/7 : EXPOSE 9000
 ---> Using cache
 ---> 997767c08e1f
Step 3/7 : EXPOSE 9001
 ---> Using cache
 ---> 97f81f4160d8
Step 4/7 : VOLUME /tmp
 ---> Using cache
 ---> 681f44ac9e48
Step 5/7 : COPY rest-server-springboot-1.0.0-SNAPSHOT.jar app.jar
 ---> 20e5f9a4eb6a
Removing intermediate container 35458a3690ef
Step 6/7 : RUN sh -c 'touch /app.jar'
 ---> Running in 07522d722e65
 ---> fc06f575b9c1
Removing intermediate container 07522d722e65
Step 7/7 : ENTRYPOINT sh -c java -Dspring-Djava.security.egd=file:/dev/./urandom -jar /app.jar --server.address=0.0.0.0
 ---> Running in 0578a5fb1c89
 ---> 131d48a13e49
Removing intermediate container 0578a5fb1c89
Successfully built 131d48a13e49
Successfully tagged rest-blueprint:latest
```

You can list now all available images using `docker images`:

```bash
$ docker images                                                                                                                                                                                                                
REPOSITORY                   TAG                     IMAGE ID            CREATED             SIZE
rest-blueprint               latest                  131d48a13e49        36 seconds ago      204MB
...
```

Start a new container based on this image:

```bash
$ docker run --rm -p 9000:9000 -p 9001:9001 rest-blueprint
...
[2017-07-03 09:29:42,094  INFO] ed.tomcat.TomcatEmbeddedServletContainer: 198 [main    ] - Tomcat started on port(s): 9000 (http)
[2017-07-03 09:29:42,097  INFO] ver.frontend.impl.springboot.Application:  57 [main    ] - Started Application in 3.897 seconds (JVM running for 4.503)
```

The option `--rm` removes the container after it has been stopped. You can ommit this.

The application now serves under `http://localhost:9000/hello`.

To stop the application just press `Ctrl-c` or use docker commands:

```bash
$ docker ps                                                                                                                                                                                                                                  
CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS              PORTS                              NAMES
5fc58c6e1508        rest-blueprint      "sh -c 'java -Dspr..."   4 minutes ago       Up 4 minutes        0.0.0.0:9000-9001->9000-9001/tcp   affectionate_johnson

$ docker stop 5fc58c6e1508
```

