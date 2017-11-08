# Initial Setup

The skeleton starts with an initial minimal setup of two files:

```
pom.xml
src/main/java/de/digitalcollections/blueprints/webapp/springboot/Application.java
```

## Basic pom.xml

As we do not want to have Spring Boot as parent (we have another one), we modify the pom.xml like described here: (see <http://docs.spring.io/spring-boot/docs/current/reference/html/using-boot-build-systems.html#using-boot-maven-without-a-parent>):

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <parent>
    <groupId>de.digitalcollections.blueprints</groupId>
    <artifactId>blueprints-parent</artifactId>
    <version>1.0.0-SNAPSHOT</version>
  </parent>

  <name>DigitalCollections: Blueprints 4: Webapp (Spring Boot + Thymeleaf)</name>
  <groupId>de.digitalcollections.blueprints</groupId>
  <artifactId>webapp-springboot-thymeleaf</artifactId>
  <version>1.0.0-SNAPSHOT</version>
  <packaging>jar</packaging>

  <properties>
    <!-- The spring-boot-starter-parent chooses fairly conservative Java compatibility. If you want to follow our recommendation and use a later Java version you can add a java.version property -->
    <java.version>1.8</java.version>
  </properties>

  <dependencyManagement>
    <dependencies>
      <dependency>
        <!-- Import dependency management from Spring Boot -->
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-dependencies</artifactId>
        <version>1.5.8.RELEASE</version>
        <type>pom</type>
        <scope>import</scope>
      </dependency>
    </dependencies>
  </dependencyManagement>

  <dependencies>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-test</artifactId>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
  </dependencies>

  <build>
    <plugins>
      <plugin>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
        <version>1.5.8.RELEASE</version>
        <executions>
          <execution>
            <goals>
              <goal>repackage</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
    </plugins>
  </build>
</project>
```
By using spring-boot-dependencies this way you can still keep the benefit of the Spring Boot dependency management, but not the plugin management. Therefore we also had to introduce the version for the spring-boot-maven-plugin.

The Spring Boot Maven plugin provides many convenient features:

- It collects all the jars on the classpath and builds a single, runnable "über-jar", which makes it more convenient to execute and transport your service.
- It searches for the public static void main() method to flag as a runnable class.
- It provides a built-in dependency resolver that sets the version number to match Spring Boot dependencies. You can override any version you wish, but it will default to Boot’s chosen set of versions.

see <https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html>

"If you don’t include the `<execution/>` configuration as above, you can run the plugin on its own (but only if the package goal is used as well). For example:

```sh
$ mvn package spring-boot:repackage
$ ls target/*.jar
target/myproject-1.0.0.jar target/myproject-1.0.0.jar.original
```

In case you want to overlay/use the resulting Spring Boot JAR as dependency in another project, you have to add the classifier `exec` to the plugin's configuration:

```xml
<plugin>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-maven-plugin</artifactId>
  <version>1.5.8.RELEASE</version>
  <executions>
    <execution>
      <goals>
        <goal>repackage</goal>
      </goals>
    </execution>
  </executions>
  <configuration>
    <classifier>exec</classifier>
  </configuration>
</plugin>
```

In this case two jar files are packaged:

- `target/webapp-springboot-thymeleaf-1.0.0-SNAPSHOT.jar` that can be used as dependency (but not be executed)
- `target/webapp-springboot-thymeleaf-1.0.0-SNAPSHOT-exec.jar` that can be executed with `java -jar`

see <http://docs.spring.io/spring-boot/docs/current/maven-plugin/examples/repackage-classifier.html>

There are three starters added as dependencies (see <http://docs.spring.io/spring-boot/docs/current/reference/html/using-boot-build-systems.html#using-boot-starter>):

- **spring-boot-starter-actuator**: Starter for using Spring Boot’s Actuator which provides production ready features to help you monitor and manage your application.
- **spring-boot-starter-test**: Starter for testing Spring Boot applications with libraries including JUnit, Hamcrest and Mockito.
- **spring-boot-starter-web**: Starter for building web, including RESTful, applications using Spring MVC. Uses Tomcat as the default embedded container.

## Basic Application.java

```java
package de.digitalcollections.blueprints.webapp.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

  public static void main(String[] args) throws Exception {
    SpringApplication.run(Application.class, args);
  }
}
```

The `@SpringBootApplication` annotation provides a load of defaults (like the embedded servlet container) depending on the contents of your classpath, and other things. It also turns on Spring MVC’s `@EnableWebMvc` annotation that activates web endpoints. `@SpringBootApplication` is a convenience annotation that adds all of the following:

 - `@Configuration` tags the class as a source of bean definitions for the application context.
 - `@EnableAutoConfiguration` tells Spring Boot to start adding beans based on classpath settings, other beans, and various property settings.
 - Normally you would add `@EnableWebMvc` for a Spring MVC app, but Spring Boot adds it automatically when it sees spring-webmvc on the classpath. This flags the application as a web application and activates key behaviors such as setting up a DispatcherServlet.
 - `@ComponentScan` tells Spring to look for other components, configurations, and services in the current package (and subpackages), allowing it to find controllers, too.

## Build a classic WAR file

see <https://spring.io/guides/gs/convert-jar-to-war/> and <http://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#build-tool-plugins-maven-packaging> and <http://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#howto-create-a-deployable-war-file>

## Build an executable JAR

see <https://spring.io/guides/gs/rest-service/>

You can build a single executable JAR file that contains all the necessary dependencies, classes, and resources, and run that. This makes it easy to ship, version, and deploy the service as an application throughout the development lifecycle, across different environments, and so forth.

```sh
$ mvn clean package
```

## Run the initial Webapp

- Using Maven:

```sh
$ mvn spring-boot:run
```

- Run executable jar (see above):

```sh
$ java -jar target/rest-server-springboot-1.0.0-SNAPSHOT-exec.jar
```

Logging output is displayed. The webapp should be up and running within a few seconds.

```

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v1.5.8.RELEASE)

...
[2017-10-16 15:09:08,279  INFO] ed.tomcat.TomcatEmbeddedServletContainer: 201 [main    ] - Tomcat started on port(s): 8080 (http)
[2017-10-16 15:09:08,283  INFO] blueprints.webapp.springboot.Application:  57 [main    ] - Started Application in 3.628 seconds (JVM running for 6.351)
```

So the server is running on port 8080, but you haven’t defined any business endpoints yet.
