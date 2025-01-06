#base version that we want to use should be mentioned like this
#make sure the image is as less memory as possible
FROM openjdk:17
#copying the jar file that is generated after running 'mvn clean install' in terminal is copied into an argument or variable
ARG JAR_FILE=target/service-registry-0.0.1-SNAPSHOT.jar
#copying the jarfile into a new named jar file, literally renaming it
COPY ${JAR_FILE} serviceregistry.jar
#defining entry point of the container, which will be started first whenever we run the container
ENTRYPOINT ["java","-jar","/serviceregistry.jar"]
#telling in which port we want to run our application, the port which is accessable by the host system
EXPOSE 8761