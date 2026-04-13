FROM eclipse-temurin:21-jre-alpine
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 4005
ENTRYPOINT ["java", "-jar", "app.jar"]