# Logging

See <https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-logging.html>

Spring Boot comes with logback by default, what we want to use because of json format for further central logging with elasticsearch and kibana.

## Configuration

### ... without own config file

Spring Boot offers several configuration properties (for `application.yml`) that are sufficient for standard logging configuration.
See <https://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html>

Excerpt:

```ini
debug=false # Enable debug logs.
trace=false # Enable trace logs.

# LOGGING
logging.config= # Location of the logging configuration file. For instance, `classpath:logback.xml` for Logback.
logging.exception-conversion-word=%wEx # Conversion word used when logging exceptions.
logging.file= # Log file name (for instance, `myapp.log`). Names can be an exact location or relative to the current directory.
logging.file.max-history=0 # Maximum of archive log files to keep. Only supported with the default logback setup.
logging.file.max-size=10MB # Maximum log file size. Only supported with the default logback setup.
logging.level.*= # Log levels severity mapping. For instance, `logging.level.org.springframework=DEBUG`.
logging.path= # Location of the log file. For instance, `/var/log`.
logging.pattern.console= # Appender pattern for output to the console. Supported only with the default Logback setup.
logging.pattern.dateformat=yyyy-MM-dd HH:mm:ss.SSS # Appender pattern for log date format. Supported only with the default Logback setup.
logging.pattern.file= # Appender pattern for output to a file. Supported only with the default Logback setup.
logging.pattern.level=%5p # Appender pattern for log level. Supported only with the default Logback setup.
logging.register-shutdown-hook=false # Register a shutdown hook for the logging system when it is initialized.
```

### ... with dedicated own config file `logback-spring.xml`

As we configure Logback using environment specific appenders, we have to use the logback-specific features that are possible in `logback-spring.xml`:

In `src/main/resources/logback-spring.xml`:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>

  <!-- see https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-logging.html#_profile_specific_configuration -->
  <springProfile name="DEV, STG, PROD">
    <appender name="default" class="ch.qos.logback.core.rolling.RollingFileAppender">
      <!-- create directory /var/log/digitalcollections with proper permissions... -->
      <file>/var/log/digitalcollections/webapp-blueprint.log</file>
      <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
        <fileNamePattern>/var/log/digitalcollections/webapp-blueprint.%d{yyyy-MM-dd}.log</fileNamePattern>
      </rollingPolicy>
      <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
        <fileNamePattern>/var/log/digitalcollections/webapp-blueprint.%d{yyyy-MM}.%i.log.gz</fileNamePattern>
        <maxFileSize>100MB</maxFileSize>
        <maxHistory>90</maxHistory>
        <totalSizeCap>5GB</totalSizeCap>
      </rollingPolicy>
      <encoder>
        <pattern>[%d{ISO8601} %5p] %40.40c:%4L [%-8t] - %m%n</pattern>
      </encoder>
      <!-- <encoder class="net.logstash.logback.encoder.LogstashEncoder">
        <customFields>{"service":"webapp-blueprint", "group":"webapps", "instance":"${instance.name:-default}"}</customFields>
      </encoder> -->
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
    
  <logger name="de.digitalcollections.blueprints" level="debug" />

  <root level="info">
    <appender-ref ref="default" />
  </root>
</configuration>
```

As you can see we added the `LogstashEncoder` as comment in our `DEV`, `STG` and `PROD` environments. Activating this (and deactivating pattern encoder) causes logback to log in JSON-format.
In our blueprint we do this to be able to collect decentralized logfiles in a central Kibana/Elasticsearch-Logging index:

```
webapp -> logback -> logstash -> filebeats (collects logging files and inserts into) -> ElasticSearch -> Kibana
```

## Switching between spring profiles

To switch between spring profiles on commandline, the `--spring.profiles.active`
option can be used with the desired profile:

```bash
java -jar target/webapp-springboot-thymeleaf-<VERSION>-exec.jar --spring.profiles.active=DEV
```

In this case the `DEV` profile is used.