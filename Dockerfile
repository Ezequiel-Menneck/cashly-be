FROM gradle:8.5-jdk17 AS builder

WORKDIR /app
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# This command will download dependencies first
RUN ./gradlew dependencies --no-daemon

# Now copy the source code
COPY src src

# Build with stacktrace for better error reporting
RUN ./gradlew bootJar --no-daemon --stacktrace

FROM eclipse-temurin:17-jre-focal

WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]