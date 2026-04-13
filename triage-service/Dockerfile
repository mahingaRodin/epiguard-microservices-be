FROM eclipse-temurin:21-jre-alpine
RUN addgroup -S spring && adduser -S spring -G spring
USE spring:spring
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 4002
ENTRYPOINT ["java", "-jar", "app.jar"]