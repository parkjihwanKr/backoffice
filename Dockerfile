# 기본 이미지로 openjdk 17 사용
FROM openjdk:17-jdk-slim

# 필수 패키지 설치 및 Tomcat 설치 (Ubuntu 기반으로 설치)
RUN apt update && apt install -y \
    curl \
    openjdk-11-jdk \
    libtcnative-1 \
    && rm -rf /var/lib/apt/lists/*

# Tomcat 10.1.14 설치 (Spring Boot 내장 톰캣 버전과 일치)
RUN curl -v -O https://downloads.apache.org/tomcat/tomcat-10/v10.1.14/bin/apache-tomcat-10.1.14.tar.gz && \
    tar -zxvf apache-tomcat-10.1.14.tar.gz -C /opt && \
    mv /opt/apache-tomcat-10.1.14 /opt/tomcat

# Tomcat Native Library 환경변수 설정
ENV LD_LIBRARY_PATH="/usr/lib:$LD_LIBRARY_PATH"

# Spring Boot 애플리케이션 JAR 파일을 컨테이너에 복사
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

# application-prod.yml 파일 복사
COPY src/main/resources/application-prod.yml /app/src/main/resources/application-prod.yml

# 애플리케이션이 실행될 포트
EXPOSE 8082

# Tomcat 실행과 함께 Spring Boot 애플리케이션 실행
CMD ["/opt/tomcat/bin/catalina.sh", "run", "&&", "java", "-Dspring.profiles.active=prod", "-jar", "/app.jar"]
