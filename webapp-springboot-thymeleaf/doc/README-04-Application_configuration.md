## Application configuration

A list of common application properties can be found here: <https://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html>

### Server Port

To change from default port 8080 to e.g. port 9000, just add this configuration to src/main/resources/application.yml:

File application.yml:

```yml
server:
  port: 9000
```

### Server header

Value to use for the Server response header (no header is sent if empty):

File application.yml:

```ini
server:
  server-header: "@project.name@ v@project.version@"
```

Example response showing server header "Webapp Blueprint v1.0.0-SNAPSHOT":

```sh
$ curl -i http://localhost:9000/health
HTTP/1.1 200 
Content-Type: application/vnd.spring-boot.actuator.v1+json;charset=UTF-8
Transfer-Encoding: chunked
Date: Mon, 16 Oct 2017 13:43:38 GMT
Server: Webapp Blueprint v1.0.0-SNAPSHOT

{"status":"UP","_links":{"self":{"href":"http://localhost:9001/health"}}}
```

### Server context path

By default the context path is "/". You can change it setting "context-path":

File application.yml:

```yml
server:
  context-path: "/blue"
```

### Disabling "Pragma no-cache"

Spring Boot activates "Pragma no-cache" header (disables caching of web resources).
If you do not want this, deactivate it in the security property:

File application.yml:

```yml
security:
  headers:
    cache: false
```

## Environment specific configuration

Until now we have only one set of configuration properties in 'application.yml'. This is ok as long we do not have different configurations per environment (e.g. spring.profiles.active=\[local, DEV, STG, PROD\]).

For environment specific configuration we can put all configurations in one file when using the YAML format (see <https://docs.spring.io/spring-boot/docs/current/reference/html/howto-properties-and-configuration.html#howto-use-yaml-for-external-properties>) as we already did (file 'application.yml'):

> YAML is a superset of JSON and as such is a very convenient syntax for storing external properties in a hierarchical format.
> Create a file called application.yml and stick it in the root of your classpath, and also add snakeyaml to your dependencies (Maven coordinates org.yaml:snakeyaml, already included if you use the spring-boot-starter). A YAML file is parsed to a Java Map<String,Object> (like a JSON object), and Spring Boot flattens the map so that it is 1-level deep and has period-separated keys, a lot like people are used to with Properties files in Java.

> A YAML file is actually a sequence of documents separated by --- lines, and each document is parsed separately to a flattened map.
> If a YAML document contains a spring.profiles key, then the profiles value (comma-separated list of profiles) is fed into the Spring Environment.acceptsProfiles() and if any of those profiles is active that document is included in the final merge (otherwise not).

Our 'application.yml' with profiles specific configuration sections is now:

```yml
endpoints:
  hypermedia:
    enabled: true

info:
  app:
    encoding: @project.build.sourceEncoding@
    java:
      source: @maven.compiler.source@
      target: @maven.compiler.target@
    project:
      name: '@project.name@'
      groupId: @project.groupId@
      artifactId: @project.artifactId@
      version: @project.version@

management:
  context-path: /actuator
  port: 9001
  security:
    enabled: false
#    roles: SUPERUSER

security:
  headers:
    cache: false
  user:
    name: admin
    password: secret

server:
  context-path: "/"
  port: 9000
  server-header: "Webapp Blueprint v@project.version@"

spring:
  profiles:
    active: local
  thymeleaf:
    cache: false

---

management:
  security:
    enabled: true

spring:
  profiles: PROD
  thymeleaf:
    cache: true
```

Additionally we set as default environment "local" (spring:profiles:active: local) in the first section. This can be overriden by passing e.g. "--spring.profiles.active=PROD" at start time. We chose to set "local" as default to avoid accidently accessing production environment during local development.

In the second section (separated by dashes) we set environment "PROD" specific configurations overriding configurations of the first section.

A list of common application properties can be found here: <https://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html>.

After starting the application you can get the list of all properties from this endpoint: <http://localhost:9001/configprops>.
