
FROM openjdk:17
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} cloudgateway.jar
#defining entry point of the container, which will be started first whenever we run the container
ENTRYPOINT ["java","-jar","/cloudgateway.jar"]
#telling in which port we want to run our application, the port which is accessable by the host system
EXPOSE 9090