FROM frolvlad/alpine-oraclejdk8:slim

EXPOSE 9000
EXPOSE 9001

VOLUME /tmp

COPY rest-server-springboot-@project.version@.jar app.jar

# ATTENTION!!!: next line makes the docker setup just usable for integration tests.
# For production user management has to be solved differently:
COPY test-classes/users.properties /local/

RUN sh -c 'touch /app.jar'

ENTRYPOINT [ "sh", "-c", "java -Dspring-Djava.security.egd=file:/dev/./urandom -jar /app.jar --server.address=0.0.0.0" ]
