FROM maven:3.9.6-eclipse-temurin-22-jammy AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:22-jdk-slim
COPY --from=build /target/AutoVault-copy-1.1.1.war demo.war
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "demo.war"]