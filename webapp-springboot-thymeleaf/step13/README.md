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
