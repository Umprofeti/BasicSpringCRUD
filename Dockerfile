FROM openjdk:17-jdk-alpine
MAINTAINER "Jonathan Rodríguez"
COPY build/libs/BasicCRUD-0.0.1-SNAPSHOT.jar BasicCRUD-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/BasicCRUD-0.0.1-SNAPSHOT.jar"]

