FROM ubuntu/jre:21-24.04_stable
LABEL authors="poerl"

WORKDIR /app

COPY build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
