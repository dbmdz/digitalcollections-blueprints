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
  server-header: "Webapp Blueprint v@project.version@"
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
