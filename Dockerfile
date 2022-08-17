FROM openjdk:17.0.1-jdk-slim

RUN addgroup --system spring && adduser --system spring
USER spring:spring

COPY ./build/libs/*-SNAPSHOT.jar app.jar

ARG ENVIRONMENT
ENV SPRING_PROFILES_ACTIVE=${ENVIRONMENT}

EXPOSE 8095

ENTRYPOINT ["java","-jar","/app.jar"]
