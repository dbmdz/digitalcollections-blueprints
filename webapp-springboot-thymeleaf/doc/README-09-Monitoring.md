# Monitoring

By already having configured actuator we have several endpoints for monitoring the webapp.

## Prometheus

Additional monitoring can be done by using e.g. [Prometheus](https://prometheus.io/).
See <https://dzone.com/articles/monitoring-using-spring-boot-2-prometheus-and-graf>

There are already two Spring Boot packages that simply can be added as Maven dependencies:

In `pom.xml`:

```xml
<dependency>
  <groupId>io.micrometer</groupId>
  <artifactId>micrometer-core</artifactId>
</dependency>
<dependency>
  <groupId>io.micrometer</groupId>
  <artifactId>micrometer-registry-prometheus</artifactId>
</dependency>
```

After adding this, an additional `actuator-endpoint` is available under `/monitoring/prometheus`:

```sh
[2018-09-27 14:05:36,645  INFO] web.servlet.WebMvcEndpointHandlerMapping: 549 [main] - Mapped "{[/monitoring/prometheus],methods=[GET],produces=[text/plain;version=0.0.4;charset=utf-8]}" onto public java.lang.Object org.springframework.boot.actuate.endpoint.web.servlet.AbstractWebMvcEndpointHandlerMapping$OperationHandler.handle(javax.servlet.http.HttpServletRequest,java.util.Map<java.lang.String, java.lang.String>)
```

The endpoint is enabled by default, see common application properties default values:

```ini
...
# METRICS ENDPOINT (MetricsEndpoint)
management.endpoint.metrics.cache.time-to-live=0ms # Maximum time that a response can be cached.
management.endpoint.metrics.enabled=true # Whether to enable the metrics endpoint.

# PROMETHEUS ENDPOINT (PrometheusScrapeEndpoint)
management.endpoint.prometheus.cache.time-to-live=0ms # Maximum time that a response can be cached.
management.endpoint.prometheus.enabled=true # Whether to enable the prometheus endpoint.
...
```

Let's see what this endpoint responds:

```sh
$ curl -u admin:secret http://localhost:9001/monitoring/prometheus
# HELP tomcat_global_received_bytes_total  
# TYPE tomcat_global_received_bytes_total counter
tomcat_global_received_bytes_total{name="http-nio-9000",} 0.0
# HELP process_files_max The maximum file descriptor count
# TYPE process_files_max gauge
process_files_max 1048576.0
...
```

These informations can be used e.g. by Grafana to visualize system metrics (see [Grafana support for Prometheus](https://prometheus.io/docs/visualization/grafana/)).

Further reading: <https://dzone.com/articles/monitoring-using-spring-boot-20-prometheus-and-gra>

## Migration Notes

### from Spring Boot 1.5.8 to Spring Boot 2.0.x

Replace

```xml
...
<version.prometheus-spring-boot-starter>1.0.2</version.prometheus-spring-boot-starter>
...
<dependency>
  <groupId>com.moelholm</groupId>
  <artifactId>prometheus-spring-boot-starter</artifactId>
  <version>${version.prometheus-spring-boot-starter}</version>
</dependency>
...
```

with

```xml
...
<version.micrometer-registry-prometheus>1.0.1</version.micrometer-registry-prometheus>
...
<dependency>
  <groupId>io.micrometer</groupId>
  <artifactId>micrometer-registry-prometheus</artifactId>
  <version>${version.micrometer-registry-prometheus}</version>
</dependency>
...
```

Add to `application.yml`:

```json
management:
  endpoints:
    web:
      base-path: '/monitoring'
      exposure:
        include:
          - prometheus
```
