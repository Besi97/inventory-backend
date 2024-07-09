FROM arm64v8/eclipse-temurin:21-jre-alpine
COPY build/libs/inventory-backend-0.0.1-beta.jar app.jar
ENTRYPOINT ["java","-Dspring.profiles.active=dev","-jar","/app.jar"]
