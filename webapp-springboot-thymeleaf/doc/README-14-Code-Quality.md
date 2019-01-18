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

## Spotbugs

SpotBugs is the successor of FindBugs and is a program which uses static analysis to look for bugs in Java code.

More information about SpotBugs website can be found [here](https://spotbugs.github.io/).

To enable it, just add the following section to your `<plugins>` section in `pom.xml`:

```xml
<plugin>
  <groupId>com.github.spotbugs</groupId>
  <artifactId>spotbugs-maven-plugin</artifactId>
  <version>3.1.8</version>
  <dependencies>
    <!-- overwrite dependency on spotbugs if you want to specify the version of spotbugs -->
    <dependency>
      <groupId>com.github.spotbugs</groupId>
      <artifactId>spotbugs</artifactId>
      <version>3.1.9</version>
    </dependency>
  </dependencies>
</plugin>
```

### Checks

To show the powerful static analysis for SpotBugs, we intentionally add a null pointer dereference bug:

```java
  private static int getLength(int i) {
    String s = null;

    if (i == 1) {
      s = "1";
    } else if (i == 2) {
      s = "2";
    }
    return s.length();
  }
```

As you can see in the example above, a null pointer dereference could occur when variable `i` is not set to `1` or `2`.

To execute the SpotBugs analysis just execute:

```bash
$ mvn spotbugs:check
```

Then a static analysis of the code is done and it will output the following:

```bash
[INFO] --- spotbugs-maven-plugin:3.1.8:check (default-cli) @ webapp-springboot-thymeleaf ---
[INFO] BugInstance size is 1
[INFO] Error size is 0
[INFO] Total bugs: 1
[ERROR] Possible null pointer dereference of s in de.digitalcollections.blueprints.webapp.springboot.controller.MainController.getLength(int) [de.digitalcollections.blueprints.webapp.springboot.controller.MainController, de.digitalcollections.blueprints.webapp.springboot.controller.MainController, de.digitalcollections.blueprints.webapp.springboot.controller.MainController] Dereferenced at MainController.java:[line 23]Null value at MainController.java:[line 16]Known null at MainController.java:[line 20] NP_NULL_ON_SOME_PATH
[INFO] 


To see bug detail using the Spotbugs GUI, use the following command "mvn spotbugs:gui"
```

Thus, SpotBugs was able to detect this null pointer dereference 🤗

**Notice**: In order to get a correct SpotBugs analysis, the `mvn clean install` command needs to be executed sucessfully. Thus, you first need to
correct the violating checkstyle rule (see previous section).

### GUI

After running the `spotbugs:check` it is also possible to run a graphical user interface, that provides more information about a specific error:

![SpotBugs - GUI](images/screenshot-spotbugs-gui.png)

The graphical user interface is shown after invoking the following command:

```bash
$ mvn spotbugs:gui
```

### Advanced

In some cases it is necessary to suppress warnings/errors that were found by SpotBugs. They can be disabled by using of Java annotations. Here comes
a list of possible annotations:


* `CheckForNull`
* `CheckReturnValue`
* `CleanupObligation`
* `Confidence`
* `CreatesObligation`
* `DefaultAnnotation`
* `DefaultAnnotationForFields`
* `DefaultAnnotationForMethods`
* `DefaultAnnotationForParameters`
* `DesireNoWarning`
* `DesireWarning`
* `DischargesObligation`
* `ExpectWarning`
* `NonNull`
* `NoWarning`
* `Nullable`
* `OverrideMustInvoke`
* `PossiblyNull`
* `Priority`
* `ReturnValuesAreNonnullByDefault`
* `SuppressFBWarnings`
* `SuppressWarnings`
* `UnknownNullness`
* `When`

E.g. the `SuppressFBWarnings` can be used to disable a SpotBugs check for a specific member variable:

```java
@JsonProperty
@SuppressFBWarnings
private String details;
```

In order to use SpotBugs annotations, the `spotbugs-annotations` package must be added to `pom.xml`:

```xml
<dependency>
  <groupId>com.github.spotbugs</groupId>
  <artifactId>spotbugs-annotations</artifactId>
  <version>3.1.10</version>
  <optional>true</optional>
</dependency>
```