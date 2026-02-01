# Build stage
FROM maven:3.9-eclipse-temurin-25 as builder

WORKDIR /app

COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn
COPY src ./src

RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:25-jre

WORKDIR /app

COPY --from=builder /app/target/estoque-*.jar app.jar

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
    CMD java -cp app.jar org.springframework.boot.loader.JarLauncher

ENTRYPOINT ["java", "-jar", "app.jar"]
