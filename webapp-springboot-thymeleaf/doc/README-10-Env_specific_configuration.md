# Environment specific configuration

Until now we have all configuration properties in one file "application.properties". This is ok as long we do not have different properties per environment (e.g. spring.profiles.active=\[local, DEV, STG, PROD\]).

But there is already one property that we have to change depending on the active environment (spring.profiles.active value): "jsondoc.basePath=http://localhost:9000"

So we could create one application.properties file per environment:

- src/main/resources/application-local.properties
- src/main/resources/application-DEV.properties
- src/main/resources/application-STG.properties
- src/main/resources/application-PROD.properties

(see <https://docs.spring.io/spring-boot/docs/current/reference/html/howto-properties-and-configuration.html#howto-change-configuration-depending-on-the-environment>)

We can put all configurations in one file when using a YAML format (see <https://docs.spring.io/spring-boot/docs/current/reference/html/howto-properties-and-configuration.html#howto-use-yaml-for-external-properties>):

> YAML is a superset of JSON and as such is a very convenient syntax for storing external properties in a hierarchical format.
> Create a file called application.yml and stick it in the root of your classpath, and also add snakeyaml to your dependencies (Maven coordinates org.yaml:snakeyaml, already included if you use the spring-boot-starter). A YAML file is parsed to a Java Map<String,Object> (like a JSON object), and Spring Boot flattens the map so that it is 1-level deep and has period-separated keys, a lot like people are used to with Properties files in Java.

> A YAML file is actually a sequence of documents separated by --- lines, and each document is parsed separately to a flattened map.
> If a YAML document contains a spring.profiles key, then the profiles value (comma-separated list of profiles) is fed into the Spring Environment.acceptsProfiles() and if any of those profiles is active that document is included in the final merge (otherwise not).

Our application.properties converted to application.yml is now:

```yml
spring:
  profiles:
    active: local

# Activate /actuator
endpoints:
  hypermedia:
    enabled: true

# Application info /info
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

# mandatory configuration
jsondoc:
  version: 1.0
  packages[0]: de.digitalcollections.template.rest.server.controller
  #packages[1]: com.example.model
  # optional configuration
  playgroundEnabled: true
  displayMethodAs: URI

# Deactivate security for sensitive actuator endpoints (DO NOT USE ON A PUBLIC ACCESSIBLE SYSTEM!!!)
management:
  port: 9001

server:
  port: 9000
---

spring:
  profiles: local
jsondoc:
  basePath: http://localhost:9000
management:
  security:
    enabled: false
---

spring:
  profiles: PROD
jsondoc:
  basePath: https://www.myrestserver.com:9000
management:
  security:
    enabled: true
```

Additionally we set as default environment "local" in the first lines. This can be overriden by passing e.g. "--spring.profiles.active=PROD" at start time.

A list of common application properties can be found here: <https://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html>.

After starting the application you can get the list of all properties from this endpoint: <http://localhost:9001/configprops>.
