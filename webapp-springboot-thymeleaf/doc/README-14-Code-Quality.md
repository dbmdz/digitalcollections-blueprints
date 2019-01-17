# Code Quality

This section covers the main code quality topics like check style or automatically finding bugs in software.

## Check style

In order to have a uniform coding style across a software developer team, it is mandatory to agree on a specifiy ruleset. 
Thus, we at [DBMDZ](https://github.com/dbmdz) use a check style for our Java software, which can be found [here](https://github.com/dbmdz/development/tree/master/code-quality).

This check style is based on the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html) and was slightly modified for our needs.

In this section the Maven checkstyle plugin is used and integrated in the main `pom.xml`. An advantage of this approach is, that every time the software
is built, all Java files are automatically analyzed. Whenever code is found that is non-compliant with our check style, an error is thrown. 

This workflow is also useful when the project is built via continuous integration (CI): non-compliant code is never going to be merged, 
when a CI pipeline is failing.

### Integration

The following sections needs to be added to `pom.xml`:

```xml
<plugin>
  <groupId>org.apache.maven.plugins</groupId>
  <artifactId>maven-checkstyle-plugin</artifactId>
  <version>3.0.0</version>
  <executions>
    <execution>
      <id>validate</id>
      <phase>validate</phase>
      <configuration>
        <configLocation>https://raw.githubusercontent.com/dbmdz/development/master/code-quality/checkstyle.xml</configLocation>
        <encoding>UTF-8</encoding>
        <consoleOutput>true</consoleOutput>
        <failsOnError>true</failsOnError>
        <linkXRef>false</linkXRef>
      </configuration>
      <goals>
        <goal>check</goal>
      </goals>
    </execution>
  </executions>
</plugin>
```

This new `plugin` section uses the `maven-checkstyle-plugin` to automatically execute style checks. Via the `<configLocation>` parameter a check style
configuration can be specified. In this example we use the publically available configuration from the [DBMDZ development](https://github.com/dbmdz/development)
repository.

Whenever a checkstyle error occurs, an error message is shown to the user. This behavior is set via the `<failsOnError>true</failsOnError>` configuration.

Other configuration options for the `maven-checkstyle-plugin` can be found [here](https://maven.apache.org/plugins/maven-checkstyle-plugin/checkstyle-mojo.html).

### Output

Whenever the coding style is violated, a detailed information is shown after the `mvn clean install` command. To demonstrate a check style violation,
a coding style error was intentionally placed into the `MainController` class.

The checkstyle forbids whitespaces after braces, like it is done in `MainController.java`:

```java
LOGGER.info(▁"Homepage requested");
```

For better visibility the `▁` character is used to show the problematic whitespace. After running `mvn clean install` the following error message is shown:

```bash
[INFO] --- maven-checkstyle-plugin:3.0.0:check (validate) @ webapp-springboot-thymeleaf ---
[INFO] Starting audit...
[WARN]  /home/.../MainController.java:15:3: Missing a Javadoc comment. [JavadocMethod]
[ERROR] /home/../MainController.java:17:17: '(' is followed by whitespace. [ParenPad]
[WARN]  /home/.../SpringConfigWeb.java:15:3: Missing a Javadoc comment. [JavadocMethod]
[WARN]  /home/.../SpringConfigWeb.java:22:3: Missing a Javadoc comment. [JavadocMethod]
Audit done.
```

Various warning are now shown, as well as the `'(' is followed by whitespace.` error.

