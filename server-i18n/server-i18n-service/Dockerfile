FROM azul/zulu-openjdk-alpine:21-latest
WORKDIR /app
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
