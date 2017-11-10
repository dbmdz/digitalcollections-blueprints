# Security configuration (unsecured webpage, secured actuator)

Unfortunately Spring Boot Actuator security secures also the webpage which should be public accessible.

To solve this, we add the following lines to application.yml:
```yml
security:
  basic:
    enabled: false
```
