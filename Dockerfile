FROM ubuntu:latest

RUN apt-get update
RUN apt-get install -y  \
    openjdk-17-jdk \
    maven 

RUN mvn package

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]