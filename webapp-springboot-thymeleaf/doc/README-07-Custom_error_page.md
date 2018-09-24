# Custom error page

By default we get the "Whitelabel error page" when an exception in the application occurs.
To replace it with an error page that uses our design / base template, we just place an `error.html` template in `src/main/resources/templates`:

File `src/main/resources/templates/error.html`:

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org"
      xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout"
      layout:decorate="~{base}">
  <body>
    <section layout:fragment="content">

      <h3>Uuups!</h3>
      
      <h1>Error <span th:text="${ status }">404</span></h1>
      <p th:text="${error}">...</p>
      <p th:text="${message}">...</p>
      <p th:text="${path}">...</p>
    </section>
  </body>
</html>
```

To avoid warnings about deprecated Thymeleaf template mode, we add to the `application.yml` configuration:

```yml
spring:
  thymeleaf:
    mode: HTML
```



## Request / Response

```sh
$  curl -i localhost:9000
HTTP/1.1 401 
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
X-Frame-Options: DENY
Strict-Transport-Security: max-age=31536000 ; includeSubDomains
WWW-Authenticate: Basic realm="Spring"
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Mon, 16 Oct 2017 14:27:51 GMT
Server: Webapp Blueprint v1.0.0-SNAPSHOT

{"timestamp":1508164071748,"status":401,"error":"Unauthorized","message":"Full authentication is required to access this resource","path":"/"}
```

Unfortunately the securing of the actuator endpoints also lead to a secured webpage controller...

```sh
$ curl -u admin:secret localhost:9000
```

returns the webpage.

To solve this issue, read next step.
