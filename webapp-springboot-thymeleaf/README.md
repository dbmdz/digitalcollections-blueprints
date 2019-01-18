# DigitalCollections Blueprints: Webapp (Spring Boot + Thymeleaf)

This project provides a best practices blueprint for a production ready Webapp skeleton based on Spring Boot and Thymeleaf.

See also the [Spring Boot Reference Guide](http://docs.spring.io/spring-boot/docs/current/reference/html/index.html).

Features documentation (each including Migration notes for version upgrades):

- [doc/README-01-Initial_setup_packaging_running.md](doc/README-01-Initial_setup_packaging_running.md): Initial Setup, Build WAR/JAR, Run webapp
- [doc/README-02-Spring_Actuator.md](doc/README-02-Spring_Actuator.md): Spring Actuator: basic endpoints, HAL browser, management port
- [doc/README-03-Application_information.md](doc/README-03-Application_information.md): Application information, project encoding
- [doc/README-04-Application_configuration.md](doc/README-04-Application_configuration.md): Application configuration (including environment specific configuration)
- [doc/README-05-Controller_endpoint_thymeleaf.md](doc/README-05-Controller_endpoint_thymeleaf.md): Controller endpoint for displaying a thymeleaf page.
- [doc/README-06-Internationalisation.md](doc/README-06-Internationalisation.md): Internationalisation (i18n) ("multilanguage GUI")
- [doc/README-07-Error_handling.md](doc/README-07-Error_handling.md): Error pages and error handling
- [doc/README-08-Logging.md](doc/README-08-Logging.md): Logging (Logback)
- [doc/README-09-Monitoring.md](doc/README-09-Monitoring.md): Monitoring
- [doc/README-10-Security.md](doc/README-10-Security.md): Security
- [doc/README-11-Unit_testing.md](doc/README-11-Unit_testing.md): Unit-Testing
- [doc/README-12-Bootstrap_migration.md](doc/README-12-Bootstrap_migration.md): Migration from Bootstrap 3 to 4
- [doc/README-13-Session_handling_via_Redis.md](doc/README-13-Session_handling_via_Redis.md): Session handling via Redis
- [doc/README-14-Code-Quality.md](doc/README-14-Code-Quality.md): Code Quality and static code analysis

## Usage

- Unsecured Webpage: <http://localhost:9000/>
- Secured Actuator endpoint (HAL Browser): <http://localhost:9001/monitoring>
- (Un)Secured Actuator health-endpoint: <http://localhost:9001/monitoring/health>

## Development Quickstart using Docker Compose

### Installation

* install Docker according to the official [Docker documentation](https://docs.docker.com/install/).

* install Docker Compose according to the official [documentation](https://docs.docker.com/compose/install/).

#### Debian 9

```sh
$ su -
# apt-get install apt-transport-https dirmngr
# echo 'deb https://apt.dockerproject.org/repo debian-stretch main' >> /etc/apt/sources.list
# apt-key adv --keyserver hkp://p80.pool.sks-keyservers.net:80 --recv-keys F76221572C52609D
Executing: /tmp/apt-key-gpghome.wDKSqs4VYM/gpg.1.sh --keyserver hkp://p80.pool.sks-keyservers.net:80 --recv-keys F76221572C52609D
gpg: key F76221572C52609D: public key "Docker Release Tool (releasedocker) <docker@docker.com>" imported
gpg: Total number processed: 1
gpg:               imported: 1
# apt-get update
# apt-get install docker-engine
# apt-get install docker-compose
```

### Configuration

#### All Linux

Add your user to docker group to run docker without sudo:

```shell
$ sudo groupadd docker
$ sudo gpasswd -a yourusername docker
$ sudo service docker restart
```

### Usage

To get the webapp quickly up running, you can start the Redis service using Docker Compose:

```shell
$ docker-compose up -d
```

Then Redis is running in a container and everything is ready for running a local instance of the webapp (see below).

To start the webapp, you have to run:

```shell
$ cd target
$ java -jar webapp-springboot-thymeleaf-<VERSION>-exec.jar
```

The webapp is now running under <http://localhost:9000/>.

To stop the container run

```shell
$ docker-compose stop
```

To delete the container and all data:

```shell
$ docker-compose down
```
