# DigitalCollections: Blueprints 5: Application (Spring Boot + Commandline) (Step 04)

This application is a commandline application for transforming a XML file with a XSL file.

## Usage

```sh
usage: java -jar transform-xml.jar <xml file> <xsl file> [-h] [-p <arg>]
  -h,--help                                Show help
  -p,--spring.profiles.active <arg>        The active spring profile
```

Example usage:

```sh
$ java -jar transform-xml.jar src/test/resources/article.xml src/test/resources/article.xsl
```