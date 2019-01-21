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

In the `application.yaml` you must define the [redis configuration](https://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html) and define the [session handling to use redis](https://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html).

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

You have to configure the embedded redis server in a Spring test configuration within the unit test. 
Since the chances are pretty high, that you have already a local redis instance running, the tests will fail.
To avoid this, you should define a `TEST`-profile for spring, where you define at least a different port
for the embedded redis instance:

```yaml

---

spring:
  profiles: TEST
  redis:
    host: localhost
    password: test
    port: 44444
```


```java
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "TEST")
class ApplicationTest {

  @TestConfiguration
  public static class EmbeddedRedisTestConfiguration {

    private final RedisServer redisServer;

    public EmbeddedRedisTestConfiguration(@Value("${spring.redis.port}") final int redisPort, @Value("${spring.redis.password}") final String redisPassword) throws IOException {
      redisServer = RedisServer.builder().port(redisPort).setting("requirepass " + redisPassword).build();
    }

    @PostConstruct
    public void startRedis() {
      redisServer.start();
    }

    @PreDestroy
    public void stopRedis() {
      this.redisServer.stop();
    }
  }

  @Autowired
  private TestRestTemplate restTemplate;


  @Test
  public void shouldReturn401WhenSendingUnauthorizedRequestToSensitiveManagementEndpoint() throws Exception {
    @SuppressWarnings("rawtypes")
    ResponseEntity<Map> entity = this.restTemplate.getForEntity(
            "http://localhost:" + this.monitoringPort + "/monitoring/env", Map.class);

    assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
  }

  @Test
  public void ensureSessionIdDoesNotChangeWhichMeansThatSessionHandlingViaRedisWorks() throws Exception {
    URI uri = URI.create("http://localhost:" + this.monitoringPort + "/monitoring/env");

    // First request with authentication
    HttpHeaders headers1 = new HttpHeaders();
    headers1.set("Authorization", "Basic " + Base64.getEncoder().encodeToString("admin:secret".getBytes()));
    RequestEntity<Object> request1 = new RequestEntity(headers1, HttpMethod.GET, uri);
    ResponseEntity<String> response1 = restTemplate.exchange(request1, String.class);
    assertThat(response1.getStatusCode()).isEqualTo(HttpStatus.OK);
    String firstSessionId = response1.getBody();
    String cookie = response1.getHeaders().getFirst("Set-Cookie");

    // Second requests uses sessionId but does not authenticate again
    HttpHeaders headers2 = new HttpHeaders();
    headers2.set("Cookie", cookie);
    RequestEntity <Object> request2 = new RequestEntity(headers2, HttpMethod.GET, uri);
    ResponseEntity<String> response2 = restTemplate.exchange(request2, String.class);
    assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.OK);
    String secondSessionId = response2.getBody();

    assertThat(firstSessionId).isEqualTo(secondSessionId);
  }
}
```