
FROM openjdk:17
ARG JAR_FILE=target/ConfigServer-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} configserver.jar
#defining entry point of the container, which will be started first whenever we run the container
ENTRYPOINT ["java","-jar","/configserver.jar"]
#telling in which port we want to run our application, the port which is accessable by the host system
EXPOSE 9296