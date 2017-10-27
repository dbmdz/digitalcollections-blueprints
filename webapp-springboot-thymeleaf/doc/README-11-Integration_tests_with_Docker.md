# Integration Tests using Docker as container for REST webservice server

Besides unit tests the next step to proof the functionality and quality of your code is to test it under production near conditions. This is called integration testing. To do so, we have to start up the server and test it by doing real requests and check the correctness of the response behavior and content.

To isolate the runtime environment from platform specific factors like operating system versions, configurations and libraries, we will run the server in well defined Docker environment (container). Doing so the test is running under the same environment circumstances, no matter where it is executed.

Note: The following Docker configuration and image also could be used in production.

## Docker configuration in (to be tested) project

Homepage Docker: <https://www.docker.com/>
Documentation: <https://docs.docker.com/get-started/>
Requirements: Docker installed on the system
Spring-Boot-Docker Guide: <https://spring.io/guides/gs/spring-boot-docker/>

As the Docker container could be used also as production container for the app, we do not just put it beside the integration test code, but place it in the application's maven module.

File `blueprints/rest-webservice/rest-server-springboot/Dockerfile`:

```
FROM frolvlad/alpine-oraclejdk8:slim

EXPOSE 9000
EXPOSE 9001

VOLUME /tmp

COPY rest-server-springboot-@project.version@.jar app.jar
RUN sh -c 'touch /app.jar'

ENTRYPOINT [ "sh", "-c", "java -Dspring-Djava.security.egd=file:/dev/./urandom -jar /app.jar --server.address=0.0.0.0" ]
```

Explanation line by line:

- `FROM frolvlad/alpine-oraclejdk8:slim`

This defines the docker image to be used as basic runtime environment ("operating system" inside the container). As we want to get a Spring Boot application up and running, we certainly need Java. This container is described as "The smallest Docker image with OracleJDK 8 (167MB)" (see <https://hub.docker.com/r/frolvlad/alpine-oraclejdk8/>). 

"slim" is the tag to be used: JDK bundle contains lots of unnecessary for Docker image stuff, so it was cleaned up. slim version: everything but compiler and jvm is removed

- `EXPOSE 9000`

see <https://docs.docker.com/engine/userguide/networking/#exposing-and-publishing-ports>:

"You expose ports using the EXPOSE keyword in the Dockerfile or the --expose flag to docker run. Exposing ports is a way of documenting which ports are used, but does not actually map or open any ports. Exposing ports is optional."

So this just informal to provide the information that our Spring Boot application will use these ports (inside container; NOT reachable on these ports from host system).

- `VOLUME /tmp`

This adds a data volume inside the container. 

see <https://docs.docker.com/engine/tutorials/dockervolumes/>:

"A data volume is a specially-designated directory within one or more containers that bypasses the Union File System. ... Data volumes are designed to persist data, independent of the container’s lifecycle. Docker therefore never automatically deletes volumes when you remove a container, nor will it “garbage collect” volumes that are no longer referenced by a container."

see <https://spring.io/guides/gs/spring-boot-docker/>

"We added a VOLUME pointing to `/tmp` because that is where a Spring Boot application creates working directories for Tomcat by default. The effect is to create a temporary file on your host under `/var/lib/docker` and link it to the container under "/tmp". This step is optional for the simple app that we wrote here, but can be necessary for other Spring Boot applications if they need to actually write in the filesystem."

- `COPY rest-server-springboot-@project.version@.jar app.jar`

see <https://docs.docker.com/engine/reference/builder/#copy>

"The COPY instruction copies new files or directories from `<src>` and adds them to the filesystem of the container at the path `<dest>`."

The placeholder `@project.version@` is replaced during the build using the maven-resource-plugin (configured in pom.xml):

```xml
<build>
  <resources>
    <resource>
      <directory>src/main/resources</directory>
      <filtering>true</filtering>
    </resource>
    <resource>
      <directory>.</directory>
      <includes>
        <include>
          Dockerfile
        </include>
      </includes>
      <targetPath>..</targetPath>
      <filtering>true</filtering>
    </resource>
  </resources>
  <plugins>
    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-resources-plugin</artifactId>
      <version>2.7</version>
      <configuration>
        <delimiters>
          <delimiter>@</delimiter>
        </delimiters>
        <useDefaultDelimiters>false</useDefaultDelimiters>
      </configuration>
    </plugin>
  ...
</build>
```

(The file `Dockerfile` resides in the root directory of the project besides the pom.xml file.)

During the build the file `rest-server-springboot-@project.version@.jar` from the target directory is copied into the file `app.jar` in the root of the container. (TODO why no path given???)

- `RUN sh -c 'touch /app.jar'`

see <https://docs.docker.com/engine/reference/builder/#run>

"The RUN instruction will execute any commands in a new layer on top of the current image and commit the results. The resulting committed image will be used for the next step in the Dockerfile."

see <https://spring.io/guides/gs/spring-boot-docker/>

"You can use a RUN command to "touch" the jar file so that it has a file modification time (Docker creates all container files in an "unmodified" state by default). This actually isn’t important for the simple app that we wrote, but any static content (e.g. "index.html") would require the file to have a modification time."

- `ENTRYPOINT [ "sh", "-c", "java -Dspring-Djava.security.egd=file:/dev/./urandom -jar /app.jar --server.address=0.0.0.0" ]`

see <https://docs.docker.com/engine/reference/builder/#entrypoint>

"ENTRYPOINT has two forms:

```
ENTRYPOINT ["executable", "param1", "param2"] (exec form, preferred)
ENTRYPOINT command param1 param2 (shell form)
```

An ENTRYPOINT allows you to configure a container that will run as an executable."

This line executes the java application `/app.jar`.

see <https://spring.io/guides/gs/spring-boot-docker/>

"To reduce Tomcat startup time we added a system property pointing to "/dev/urandom" as a source of entropy."

To make the app listen all interfaces we add `--server.address=0.0.0.0`.

Now the image could be build and started like this:

```bash
$ mvn clean package
$ cd target
$ docker build . -t rest-blueprint
$ docker run --rm -p 9000:9000 -p 9001:9001 rest-blueprint
```

## Integration Test Project using TestContainers library

Requirements: Docker Compose installed on the system (<https://docs.docker.com/compose/install/>)

To start the image during integrations tests we create an integration test project, that will use
the above Docker configuration for starting up the REST-webservice application.

For starting up the docker image from inside an integration test, we use the TestContainers library (<https://www.testcontainers.org/>).

"TestContainers is a Java library that supports JUnit tests, providing lightweight, throwaway instances of common databases, Selenium web browsers, or anything else that can run in a Docker container. ... TestContainers makes it easy to launch useful Docker containers for the duration of JUnit tests."

Within the framework we use the "Docker Compose" (<https://docs.docker.com/compose/>) container type to get a container up and running with docker compose (see <https://www.testcontainers.org/usage/docker_compose.html>):

"Similar to generic containers support, it's also possible to run a bespoke set of services specified in a docker-compose.yml file.

This is intended to be useful on projects where Docker Compose is already used in dev or other environments to define services that an application may be dependent upon.
Behind the scenes, TestContainers actually launches a temporary Docker Compose client - in a container, of course, so it's not necessary to have it installed on all developer/test machines." (we will use a local installed docker compose, see below "withLocalCompose"...)

### Configuration:

#### Add TestContainers dependency

File `blueprints/rest-webservice/rest-integration-tests/pom.xml`:

```xml
...
  <properties>
    <testcontainers.version>1.3.0</testcontainers.version>
  </properties>
...
  <dependencies>
    <dependency>
      <groupId>org.testcontainers</groupId>
      <artifactId>testcontainers</artifactId>
      <version>${testcontainers.version}</version>
      <scope>test</scope>
    </dependency>
  </dependencies>
```

#### Add Docker Compose configuration

File `blueprints/rest-webservice/rest-integration-tests/src/test/resources/docker-compose.yml`:

```yml
version: '2'
services:
  rest:
    build: "../../../../../rest-webservice/rest-server-springboot/target"
    ports:
     - "9000"
     - "9001"
```

The Compose file is a YAML file defining services, networks and volumes. The default path for a Compose file is `./docker-compose.yml` (see <https://docs.docker.com/compose/compose-file/compose-file-v2/>)

This configuration specifies 

- `version:` the version of the Compose file format ("2") 
- `services:` start definition section for services
- `rest:`: start specification section for service with name "rest"
- `build:`: specifies the path to the build context. In our case it is the directory `../../../../../rest-webservice/rest-server-springboot/target` (the target directory of the parallel project; sorry for the directory upclimbing...) containing the `Dockerfile`.
- `ports:` "Expose ports. Either specify both ports (HOST:CONTAINER), or just the container port (a random host port will be chosen)." (see <https://docs.docker.com/compose/compose-file/compose-file-v2/#ports>). As we want the inner ports 9000 and 9001 be mapped to a random (free) port, we just specify the container ports.

### Usage in an integration test

see <https://www.testcontainers.org/usage/docker_compose.html>

```java
package de.digitalcollections.blueprints.rest.integration.tests;

import de.digitalcollections.blueprints.rest.client.HelloClient;
import de.digitalcollections.blueprints.rest.common.model.impl.Greeting;
import feign.Feign;
import feign.gson.GsonDecoder;
import java.io.File;
import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.DockerComposeContainer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.from;

public class HelloClientTest {

  private static final String DOCKER_COMPOSE_FILE = "src/test/resources/docker-compose.yml";

  private static final String REST_SERVICE_NAME = "rest_1";

  private static final int REST_SERVICE_PORT = 9000;

  @ClassRule
  public static final DockerComposeContainer ENVIRONMENT = new DockerComposeContainer(new File(DOCKER_COMPOSE_FILE))
          .withLocalCompose(true)
          .withExposedService(REST_SERVICE_NAME, REST_SERVICE_PORT)
          .withTailChildContainers(true); // append all outputs of applications in the container to the main log

  @Test
  public void greetingShouldReturnGreetingForTheExpectedName() {
    final String targetUrl = String.format("http://%s:%d",
                                           ENVIRONMENT.getServiceHost(REST_SERVICE_NAME, REST_SERVICE_PORT),
                                           ENVIRONMENT.getServicePort(REST_SERVICE_NAME, REST_SERVICE_PORT));

    final HelloClient client = Feign.builder()
            .decoder(new GsonDecoder())
            .target(HelloClient.class, targetUrl);

    assertThat(client.greeting("Captain")).returns("Hello, Captain!", from(Greeting::getContent));
  }

}
```

- Add the Docker Compose environment as `@ClassRule`.
- Point the DockerComposeContainer to the location of the docker-compose.yml file.
- Specify ".withLocalCompose(true)" to use local installed docker-compose to make it possible for docker-compose to access files outside container.
- Specify ".withExposedService(serviceName, servicePort)" to get access to the "rest"-service. Name is "rest_1", because we want access to the first (and only) instance of the service. As port specifiy the inner port, the random chosen external port will be automatically mapped.
- Specify ".withTailChildContainers(true)" to append all application outputs from inside the container to the main log (e.g. for debugging).

Accessing the service from inside the test method is done by constructing the HTTP-Url based on this environment:

```java
final String targetUrl = String.format("http://%s:%d",
                                        ENVIRONMENT.getServiceHost(REST_SERVICE_NAME, REST_SERVICE_PORT),
                                        ENVIRONMENT.getServicePort(REST_SERVICE_NAME, REST_SERVICE_PORT));
```
