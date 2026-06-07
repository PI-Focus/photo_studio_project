FROM ubuntu/jdk:21-24.04_stable
LABEL authors="poer1"

WORKDIR /app

COPY build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
