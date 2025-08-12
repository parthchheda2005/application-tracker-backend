FROM openjdk:24-jdk-slim

WORKDIR /app

COPY mvnw pom.xml ./
COPY .mvn .mvn

RUN ./mvnw dependency:go-offline -B

COPY src ./src

RUN ./mvnw package -DskipTests

EXPOSE 8080

CMD ["java", "-jar", "target/applicationtrackerbackend-0.0.1-SNAPSHOT.jar"]