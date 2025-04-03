FROM openjdk:21-slim
USER root
RUN apt update && apt install -y redis sudo
COPY capston-spring-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
