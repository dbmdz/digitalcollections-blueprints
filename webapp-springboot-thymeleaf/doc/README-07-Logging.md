# Logging

Spring Boot comes with logback by default, what we want to use because of json format for further central logging with elasticsearch and kibana.

## Configuration

We configure Logback using environment specific appenders. Be sure the config file is named "logback-spring.xml" to be able to use this feature:

src/main/resources/logback-spring.xml:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>

  <!-- see https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-logging.html#_profile_specific_configuration -->
  <springProfile name="DEV, STG, PROD">
    <appender name="default" class="ch.qos.logback.core.rolling.RollingFileAppender">
      <!-- create directory /var/log/digitalcollections with proper permissions... -->
      <file>/var/log/digitalcollections/rest-server.log</file>
      <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
        <fileNamePattern>rest-server.%d{yyyy-MM-dd}.log</fileNamePattern>
      </rollingPolicy>
      <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
        <fileNamePattern>rest-server.%d{yyyy-MM}.%i.log.gz</fileNamePattern>
        <maxFileSize>100MB</maxFileSize>
        <maxHistory>90</maxHistory>
        <totalSizeCap>5GB</totalSizeCap>
      </rollingPolicy>
      <encoder class="net.logstash.logback.encoder.LogstashEncoder">
        <customFields>{"service":"rest-server", "group":"rest", "instance":"${instance.name:-default}"}</customFields>
      </encoder>
    </appender>
  </springProfile>
  
  <springProfile name="local">
    <appender name="default" class="ch.qos.logback.core.ConsoleAppender">
      <encoder>
        <pattern>[%d{ISO8601} %5p] %40.40c:%4L [%-8t] - %m%n</pattern>
      </encoder>
    </appender>
  </springProfile>
  
  <!-- if no environment of above is set add console log as fallback -->
  <!-- documented feature, but working with OR, so can not be used this way! -->
  <!-- see: https://github.com/spring-projects/spring-boot/issues/5851 -->
  <!--
  <springProfile name="!local, !DEV, !STG, !PROD">
    <appender name="default" class="ch.qos.logback.core.ConsoleAppender">
      <encoder>
        <pattern>[%d{ISO8601} %5p] %40.40c:%4L [%-8t] - %m%n</pattern>
      </encoder>
    </appender>
  </springProfile>
  -->
    
  <logger name="de.digitalcollections.template" level="debug" />

  <root level="info">
    <appender-ref ref="default" />
  </root>
</configuration>
```

As you can see we added the LogstashEncoder. This causes logback to log in JSON-format. In our blueprint we do this to be able to collect dentralized logfiles in a central Kibana/Elasticsearch-Logging index:

```
webapp -> logback -> logstash -> filebeats (collects logging files and inserts into) -> ElasticSearch -> Kibana
```
