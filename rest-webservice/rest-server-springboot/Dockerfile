FROM frolvlad/alpine-oraclejdk8:slim

EXPOSE 9000
EXPOSE 9001

VOLUME /tmp

COPY target/rest-server-springboot-1.0.0-SNAPSHOT.jar app.jar
RUN sh -c 'touch /app.jar'

ENTRYPOINT [ "sh", "-c", "java -Dspring-Djava.security.egd=file:/dev/./urandom -jar /app.jar --server.address=0.0.0.0" ]
