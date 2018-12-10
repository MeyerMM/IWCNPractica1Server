FROM java:8
VOLUME /tmp
EXPOSE 8081
ADD /build/libs/practica1server-0.0.1-SNAPSHOT.jar server.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","server.jar"]