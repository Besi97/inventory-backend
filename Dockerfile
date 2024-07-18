FROM arm64v8/eclipse-temurin:21-jre-alpine
WORKDIR /workspace
COPY build/libs/inventory-backend-*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
