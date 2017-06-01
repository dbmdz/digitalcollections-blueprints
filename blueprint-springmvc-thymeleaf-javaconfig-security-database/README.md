# DigitalCollections: Blueprints 2: Spring MVC + Thymeleaf + JavaConfig + Security +JPA/Hibernate

This blueprint webapp is demonstrating the integration of fundamental technologies for a Spring Java webapp.

Technologies used:

* Overall: Java, Spring, Spring Security
* Frontend: Spring MVC, Thymeleaf
* Business: Java
* Backend: JPA/Hibernate, Flyway

Features:

* Automatic admin user wizard.
* Multilingual GUI.
* Login/logout.
* User management (CRUD)
* Session logging incl. AOP-logging
* Layer modularization (Frontend, Business, Backend; each API and IMPL)

* Maven Site

## Installation

1.  Install PostgreSql:

    on Ubuntu:

```sh
$ apt-cache search postgresql
...
postgresql - object-relational SQL database (supported version)
postgresql-9.4 - object-relational SQL database, version 9.4 server
...
$ sudo apt-get install postgresql
```

2.  Create a database on your PostgreSql instance:

```sh
$ sudo su - postgres
($ dropdb 'blue_db')
$ psql -c "CREATE USER blue PASSWORD 'somepassword';"
CREATE ROLE
$ createdb blue_db -O blue
```

    Check:

    List databases:

```sh
$ psql -l
                       List of databases
Name    |  Owner   | Encoding |   Collate   |    Ctype    |   Access privileges   
-----------+----------+----------+-------------+-------------+-----------------------
postgres  | postgres | UTF8     | en_US.UTF-8 | en_US.UTF-8 | 
template0 | postgres | UTF8     | en_US.UTF-8 | en_US.UTF-8 | =c/postgres          +
          |          |          |             |             | postgres=CTc/postgres
template1 | postgres | UTF8     | en_US.UTF-8 | en_US.UTF-8 | =c/postgres          +
          |          |          |             |             | postgres=CTc/postgres
blue_db   | blu      | UTF8     | en_US.UTF-8 | en_US.UTF-8 | 
(4 rows)
```

    List tables of database blue_db:

```sh
$ psql -d blue_db
psql (9.4.1)
Type "help" for help.
blue_db=# \d
No relations found.
blue_db=# \q
```

3. Put your database properties into configuration file(s):

```sh
$ cd <source directory>
$ vi dc-blueprints-crud-hibernate-backend-impl-jpa/src/main/resources/de/digitalcollections/blueprints/crud/config/SpringConfigBackend-<profile>.properties

database.name=blue_db
database.hostname=localhost
database.password=somepassword
database.port=5432
database.username=blue
```

## Build

```sh
$ cd <source directory>
$ mvn clean install
```

## Usage

Run (in development)

```sh
$ cd dc-blueprints-crud-hibernate-frontend-impl-webapp
$ mvn jetty:run
```

Run (in production)

* Deploy WAR to Tomcat
* Start with java environment variable "spring.profiles.active" set to "PROD" (-Dspring.profiles.active:PROD)

View

    Browser: http://localhost:9898

The webapp connects to the database and if no admin user exists, the admin user creation assistant is launched.
Create an admin user.