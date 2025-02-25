FROM gradle:jdk21 AS builder
WORKDIR /application
COPY build.gradle settings.gradle gradlew ./
COPY gradle/ gradle/
COPY src/ src/
RUN gradle clean bootJar

FROM openjdk:21
LABEL authors="okhapkin_mikhail"

ENV TZ="Europe/Moscow"
RUN date

WORKDIR /application
COPY --from=builder /application/build/libs/*.jar ./taskManager.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","taskManager.jar"]