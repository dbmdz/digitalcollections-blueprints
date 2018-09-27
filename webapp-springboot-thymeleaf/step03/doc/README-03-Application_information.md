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
So we remove duplicate project-values from `application.yml`:

```yml
info:
  app:
    ...
#    project:
#      name: '@project.name@'
#      groupId: @project.groupId@
#      artifactId: @project.artifactId@
#      version: @project.version@
```

(Finally we deleted the lines...)

### TODO: Add Git information

The following section is not complete. Git information file git.properties is not generated...

As we manage our sourcecode with Git, we can also include Git informations to the application infos.
See

* <https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#production-ready-application-info-git>
* <https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#howto-git-info>

#### `pom.xml`

```xml
<plugin>
  <groupId>pl.project13.maven</groupId>
  <artifactId>git-commit-id-plugin</artifactId>
  <version>2.2.5</version>
  <configuration>
    <!--
      If you'd like to tell the plugin where your .git directory is,
      use this setting, otherwise we'll perform a search trying to
      figure out the right directory. It's better to add it explicitly IMHO.
    -->
    <dotGitDirectory>${project.basedir}/../../.git</dotGitDirectory>
  </configuration>
</plugin>
```

TODO...
