# Application information

See <http://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#production-ready-application-info>

Application information exposes various information collected from all InfoContributor beans defined in your `ApplicationContext`. Spring Boot includes a number of auto-configured `InfoContributors` and you can also write your own.

You can customize the data exposed by the info endpoint by setting `info.*` Spring properties. All Environment properties under the `info` key will be automatically exposed. For example, you could add the following to your `application.yml`:

## `application.yml`

```yml
info:
  app:
    encoding: @project.build.sourceEncoding@
    java:
      source: @maven.compiler.source@
      target: @maven.compiler.target@
```

To expand info properties **at build time** (so you have to recompile if you change `application.yml`) we also have to configure Maven accordingly (see <http://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#howto-automatic-expansion>).

## `pom.xml`

```xml
...
<build>
  <resources>
    <resource>
      <directory>src/main/resources</directory>
      <filtering>true</filtering>
    </resource>
  </resources>
  <plugins>
    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-resources-plugin</artifactId>
      <version>3.1.0</version>
      <configuration>
        <delimiters>
          <delimiter>@</delimiter>
        </delimiters>
        <useDefaultDelimiters>false</useDefaultDelimiters>
      </configuration>
    </plugin>
    ...
  </plugins>
  ...
</build>
```

The `useDefaultDelimiters` property is important if you are using standard Spring placeholders in your configuration (e.g. `${foo}`). These may be expanded by the build if that property is not set to `false`.

## Maven Compiler configuration

To output the dedicated source and target compiler version, we add the Maven compiler plugin and the according properties (see <https://maven.apache.org/plugins/maven-compiler-plugin/examples/set-compiler-source-and-target.html>).

Even if the info output already has shown "UTF-8" as project encoding, it is safer to explicitely add it to the properties with the default key `project.build.sourceEncoding`:

### `pom.xml`

```xml
<properties>
  <maven.compiler.source>1.8</maven.compiler.source>
  <maven.compiler.target>1.8</maven.compiler.target>
  <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
</properties>
...
<build>
  ...
  <plugins>
    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-compiler-plugin</artifactId>
      <version>3.8.0</version>
      <configuration>
        <showDeprecation>true</showDeprecation>
        <encoding>${project.build.sourceEncoding}</encoding>
        <source>${maven.compiler.source}</source>
        <target>${maven.compiler.target}</target>
      </configuration>
    </plugin>
    ...
  </plugins>
  ...
</build>
```

### `application.yml`

```yml
info:
  app:
    encoding: @project.build.sourceEncoding@
    java:
      source: @maven.compiler.source@
      target: @maven.compiler.target@
```

## Project information

It also seems to be a good idea to output information about the application itself:

### `application.yml`

```yml
info:
  app:
    ...
    project:
      name: '@project.name@'
      groupId: @project.groupId@
      artifactId: @project.artifactId@
      version: @project.version@
```

### Response Sample

Response to <http://localhost:9001/monitoring/info>:

```json
{
  "app": {
    "project": {
      "name": "DigitalCollections: Blueprints 4: Webapp (Spring Boot + Thymeleaf)",
      "groupId": "de.digitalcollections.blueprints",
      "version": "1.0.0-SNAPSHOT",
      "artifactId": "webapp-springboot-thymeleaf"
    },
    "java": {
      "source": "1.8",
      "target": "1.8"
    },
    "encoding": "UTF-8"
  }
}
```

### Add build details

#### `pom.xml`

```xml
<plugin>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-maven-plugin</artifactId>
  <executions>
    <execution>
      <goals>
        <goal>build-info</goal>
        <goal>repackage</goal>
      </goals>
    </execution>
  </executions>
</plugin>
```

#### Sample Response

```json
{
  "app": {
    "encoding": "UTF-8",
    "java": {
      "source": 1.8,
      "target": 1.8
    },
    "project": {
      "name": "DigitalCollections: Blueprints 4: Webapp (Spring Boot + Thymeleaf)",
      "groupId": "de.digitalcollections.blueprints",
      "artifactId": "webapp-springboot-thymeleaf",
      "version": "1.0.0-SNAPSHOT"
    }
  },
  "build": {
    "version": "1.0.0-SNAPSHOT",
    "artifact": "webapp-springboot-thymeleaf",
    "name": "DigitalCollections: Blueprints 4: Webapp (Spring Boot + Thymeleaf)",
    "group": "de.digitalcollections.blueprints",
    "time": "2018-05-02T11:39:11.772Z"
  }
}
```

The build info duplicates our project info and adds a timestamp.
We could remove duplicate project-values from `application.yml`, but finally left them there as other info endpoints rely on them:

```yml
info:
  app:
    ...
    project:
      name: '@project.name@'
      groupId: @project.groupId@
      artifactId: @project.artifactId@
      version: @project.version@
```

(Finally we deleted the lines...)

### Add Git information

As we manage our sourcecode with Git, we can also include Git informations to 
the application infos. See

* <https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#production-ready-application-info-git>
* <https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#howto-git-info>
* <https://github.com/ktoso/maven-git-commit-id-plugin>

#### Configuration

The following options for configuring the Git plugin need to be added in `pom.xml`:

```xml
<plugin>
  <groupId>pl.project13.maven</groupId>
  <artifactId>git-commit-id-plugin</artifactId>
  <version>2.2.5</version>
  <executions>
    <execution>
      <id>get-the-git-infos</id>
      <goals>
        <goal>revision</goal>
      </goals>
    </execution>
    <execution>
      <id>validate-the-git-infos</id>
      <goals>
        <goal>validateRevision</goal>
      </goals>
    </execution>
  </executions>
  <configuration>
    <dotGitDirectory>${project.basedir}/../../.git</dotGitDirectory>
    <generateGitPropertiesFile>true</generateGitPropertiesFile>
  </configuration>
</plugin>
```

To get complete information from Git, the following `actuator` info
endpoint configuration for the Git plugin is needed:

```yaml
management:
  info:
    git:
      mode: full
```

#### Output

Then the Git plugin shows the following information under 
<http://localhost:9001/monitoring/info>:

```json
{
  "git":{
    "tags":"",
    "build":{
      "version":"1.0.0-SNAPSHOT",
      "user":{
        "email":"firstname.lastname@bsb-muenchen.de",
        "name":"Firstname Lastname"
      },
      "time":"2018-10-08T11:54:12Z",
      "host":"user-power-workstation"
    },
    "total":{
      "commit":{
        "count":"123"
      }
    },
    "commit":{
      "time":"2018-10-05T11:20:44Z",
      "user":{
        "name":"Firstname Lastname",
        "email":"firstname.lastname@bsb-muenchen.de"
      },
      "message":{
        "full":"step06: recover files for step06",
        "short":"step06: recover files for step06"
      },
      "id":{
        "abbrev":"19bc580",
        "describe-short":"19bc580-dirty",
        "full":"19bc5806800fdf8f98f20f5ca8796058089a1e13",
        "describe":"19bc580-dirty"
      }
    },
    "branch":"recover-step06",
    "closest":{
      "tag":{
        "name":"",
        "commit":{
          "count":""
        }
      }
    },
    "remote":{
      "origin":{
        "url":"https://github.com/dbmdz/blueprints.git"
      }
    },
    "dirty":"true"
  }
}
```

### Add detailed version information

See <https://github.com/dbmdz/digitalcollections-commons/tree/master/dc-commons-springboot>

The DigitalCollections Commons project provides Spring Boot specific extensions. One of them is the "VersionInfo" extension to provide build sepcific informations:

  "VersionInfoBean: This bean reads the build information from the application.yml, which is set by the maven build process and also automatically collects all version information from all dependencies (jar files)."

#### Add dependency into `pom.xml`

```xml
<dependency>
  <groupId>de.digitalcollections.commons</groupId>
  <artifactId>dc-commons-springboot</artifactId>
  <version>2.0.0-SNAPSHOT</version>
</dependency>
```

#### `SpringConfig.java`

To automatically detect the version info Spring beans of the `dc-commons-springboot`-library, we add a new Java class `src/main/java/de/digitalcollections/blueprints/webapp/springboot/config/SpringConfig.java`:

```java
package de.digitalcollections.blueprints.webapp.springboot.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
  "de.digitalcollections.commons.springboot.actuator",
  "de.digitalcollections.commons.springboot.contributor",
  "de.digitalcollections.commons.springboot.monitoring"
})
public class SpringConfig {

}
```

#### `application.yml`

To include the new `version` actuator endpoint it has to be exposed through configuration in `application.yml`.

```yml
management:
  endpoints:
    web:
      exposure:
        include:
          - version
```

As we already expose all (`include: "*"`) this is already done.

Finally add `buildDetails` properties to the `info`-section:

```yml
info:
  app:
    project:
      name: '@project.name@'
      groupId: @project.groupId@
      artifactId: @project.artifactId@
      version: @project.version@
      buildDetails: '@versionName@'
```

#### `pom.xml`

To fill `versionName` for the `info`-endpoint during filtering of resources (`application.yml`), we add the following properties to `pom.xml`:

```xml
<properties>
  <timestamp>${maven.build.timestamp}</timestamp>
  <maven.build.timestamp.format>yyyy-MM-dd HH:mm:ss</maven.build.timestamp.format>
  <versionName>${project.version} manually built by ${user.name} at ${maven.build.timestamp}</versionName>
</properties>
```

Example output from `http://localhost:9001/monitoring/info`:

```json
{
  "app": {
    "encoding": "UTF-8",
    "java": {
      "source": 1.8,
      "target": 1.8
    },
    "project": {
      "name": "DigitalCollections: Blueprints 4: Webapp (Spring Boot + Thymeleaf) - Step 10",
      "artifactId": "webapp-springboot-thymeleaf",
      "version": "1.0.0-SNAPSHOT",
      "buildDetails": "1.0.0-SNAPSHOT manually built by ralf at 2018-10-18 11:29:32"
    }
  },
...
```

Example output from `http://localhost:9001/monitoring/version`:

```json
{
  "name": "DigitalCollections: Blueprints 4: Webapp (Spring Boot + Thymeleaf) - Step 10",
  "version": "1.0.0-SNAPSHOT",
  "details": "1.0.0-SNAPSHOT manually built by ralf at 2018-10-18 14:14:33"
}
```

If your project is versioned in GitLab, more detailled informations can be added, see <https://github.com/dbmdz/digitalcollections-commons/tree/master/dc-commons-springboot#versioninfobean>