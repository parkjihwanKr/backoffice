# java slim 버전
FROM openjdk:17-jdk-slim
VOLUME /tmp
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
COPY src/main/resources/application-prod.yml /app/src/main/resources/application-prod.yml
EXPOSE 8082
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "/app.jar"]