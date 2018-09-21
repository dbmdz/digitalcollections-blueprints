## Application configuration

A list of common application properties can be found here: <https://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html>

### Server Port

To change from default port 8080 to e.g. port 9000, just add this configuration to `src/main/resources/application.yml`:

In `application.yml`:

```yml
server:
  port: 9000
```

### Server header

Value to use for the Server response header (no header is sent if empty):

File `application.yml`:

```ini
server:
  server-header: "@project.name@ v@project.version@"
```

Example monitoring-response showing server header `Webapp Blueprint v1.0.0-SNAPSHOT`:

```sh
$ curl -i -u admin:secret http://localhost:9001/monitoring/health
HTTP/1.1 200 
Set-Cookie: JSESSIONID=68C80283AC05E572B92D224CBC87353C; Path=/; HttpOnly
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Frame-Options: DENY
Content-Type: application/vnd.spring-boot.actuator.v2+json;charset=UTF-8
Transfer-Encoding: chunked
Date: Fri, 21 Sep 2018 09:24:12 GMT
Server: DigitalCollections: Blueprints 4: Webapp (Spring Boot + Thymeleaf) - Step 04 v1.0.0-SNAPSHOT


{"status":"UP","details":{"diskSpace":{"status":"UP","details":{"total":120906379264,"free":4989669376,"threshold":10485760}}}}
```

But also users (in internet) now get the header-information!

```sh
$ curl -i http://localhost:9000/
HTTP/1.1 404 
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Fri, 21 Sep 2018 09:26:38 GMT
Server: DigitalCollections: Blueprints 4: Webapp (Spring Boot + Thymeleaf) - Step 04 v1.0.0-SNAPSHOT

{"timestamp":"2018-09-21T09:26:38.452+0000","status":404,"error":"Not Found","message":"No message available","path":"/"}
```

### Server context path

By default the context path is `/`. You can change it setting `context-path`:

In `application.yml`:

```yml
server:
  servlet:
    context-path: "/blue"
```

This is just for information! We do not change our context path in our blueprint.
But we define it (to the default value) to make it more visible to the developer that it can be changed:

```yml
server:
  servlet:
    context-path: "/"
```

## Environment specific configuration

Until now we have only one set of configuration properties in `application.yml`.
This is ok as long we do not have different configurations per environment (e.g. `spring.profiles.active=\[local, DEV, STG, PROD\]`).

For environment specific configuration we can put all configurations in one file when using the YAML format
(see <https://docs.spring.io/spring-boot/docs/current/reference/html/howto-properties-and-configuration.html#howto-use-yaml-for-external-properties>) as we already did (file `application.yml`):

> YAML is a superset of JSON and as such is a very convenient syntax for storing external properties in a hierarchical format.
> Create a file called application.yml and stick it in the root of your classpath, and also add snakeyaml to your dependencies (Maven coordinates org.yaml:snakeyaml, already included if you use the spring-boot-starter). A YAML file is parsed to a Java Map<String,Object> (like a JSON object), and Spring Boot flattens the map so that it is 1-level deep and has period-separated keys, a lot like people are used to with Properties files in Java.

> A YAML file is actually a sequence of documents separated by --- lines, and each document is parsed separately to a flattened map.
> If a YAML document contains a spring.profiles key, then the profiles value (comma-separated list of profiles) is fed into the Spring Environment.acceptsProfiles() and if any of those profiles is active that document is included in the final merge (otherwise not).

Our `application.yml` with profiles specific configuration sections is now:

```yml
info:
  app:
    encoding: @project.build.sourceEncoding@
    java:
      source: @maven.compiler.source@
      target: @maven.compiler.target@

management:
  endpoint:
    health:
      show-details: when-authorized
  endpoints:
    web:
      base-path: '/monitoring'
      exposure:
        include: '*'
  server:
    port: 9001

server:
  port: 9000
  server-header: "@project.name@ v@project.version@"
  servlet:
    context-path: "/"

spring:
  profiles:
    active: local
  security:
    user:
      name: admin
      password: secret
      roles: ENDPOINT_ADMIN

---

spring:
  profiles: PROD
```

Additionally we set as default environment `local` (`spring:profiles:active: local`) in the first section.
This can be overriden by passing e.g. `--spring.profiles.active=PROD` at start time.
We chose to set `local` as default to avoid accidentally accessing production environment during local development.

In the second section (separated by dashes) we set environment `PROD` specific configurations overriding configurations of the first section.
(We do not have different properties in place, yet. So properties are all inherited from default profile above).

During startup the active profile name is logged:

```sh
2018-09-21 13:32:17.041  INFO 12501 --- [main] d.d.b.webapp.springboot.Application: The following profiles are active: local
```

A list of common application properties can be found here: <https://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html>.

After starting the application you can get the list of all properties from this endpoint: <http://localhost:9001/monitoring/configprops>.
