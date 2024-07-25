FROM openjdk:17-jdk-slim
VOLUME /tmp
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
COPY src/main/resources/application-prod.yml /app/src/main/resources/application-prod.yml
EXPOSE 8082
ENTRYPOINT ["java","-jar","/app.jar"]