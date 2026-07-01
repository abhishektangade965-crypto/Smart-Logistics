FROM eclipse-temurin:17-jre-slim
WORKDIR /app
COPY target/smart-logistics-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
