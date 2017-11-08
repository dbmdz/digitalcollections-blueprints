# Monitoring

By already having configured actuator we have several endpoints for monitoring the webapp.

## Prometheus

Additional monitoring can be done by using e.g. [Prometheus](https://prometheus.io/).

There is already a Spring Boot package that simply can be added as Maven dependency:

pom.xml:

```xml
<dependency>
  <groupId>com.moelholm</groupId>
  <artifactId>prometheus-spring-boot-starter</artifactId>
  <version>1.0.2</version>
</dependency>
```

After adding this, an additional actuator-endpoint is available:

```sh
$ curl -u foobar:hamspam localhost:9001/monitoring/prometheus
# HELP httpsessions_max httpsessions_max
# TYPE httpsessions_max gauge
httpsessions_max -1.0
# HELP httpsessions_active httpsessions_active
# TYPE httpsessions_active gauge
httpsessions_active 0.0
# HELP mem mem
# TYPE mem gauge
mem 639499.0
...
```

This can be used e.g. by Grafana to visualize system metrics (see [Grafana support for Prometheus](https://prometheus.io/docs/visualization/grafana/)).