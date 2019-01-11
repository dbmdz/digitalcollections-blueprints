# Session handling via Redis

As soon as you have more than one instance of a webapp running in a loadbalanced environment, you cannot use the standard tomcat HTTP session any more,
but you need to share it over all your instances.

Using [Redis](https://redis.io/) as a central session storage is a common solution. 

This tutorial assumes, that you already have a Redis server available at `localhost:6379`.

## Add dependencies

Add the following dependencies to your `pom.xml`. The versions of them are handled by the `spring-boot` and `springframework` dependency management.
For testing, you can use [embedded-redis](https://github.com/ozimov/embedded-redis).

```xml
<properties>
  ...
  <version.embedded-redis>0.7.2</version.embedded-redis>
  ...
</properties>
  
<dependencies>
  ...
  <dependency>
    <groupId>it.ozimov</groupId>
    <artifactId>embedded-redis</artifactId>
    <version>${version.embedded-redis}</version>
    <scope>test</scope>
  </dependency>
  ...
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
  </dependency>
  ...
  <dependency>
    <groupId>org.springframework.session</groupId>
    <artifactId>spring-session-data-redis</artifactId>
  </dependency>
</dependencies>
```

## Add configuration properties

In the `application.properties` you must define the [redis configuration](https://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html) and define the [session handling to use redis](https://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html).

Additionally, you can also set some configuration parameters like the session timeout or the namespace, your instance uses:

```yaml
spring:
  redis:
    host: localhost
    port: 6379
  session:
    redis:
      namespace: "blueprint:session"
    store-type: redis
    timeout: 24h
```

## Implementation caveats

The only thing in your implementation you have to take care of is, that the session data must implement the `Serializable` interface.

## Testing

The integration test uses an [embedded redis](https://github.com/ozimov/embedded-redis) instance and checks, if the authentication survives when re-using the session id for the second request.