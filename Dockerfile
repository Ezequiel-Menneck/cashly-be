# Stage 1: Build
FROM gradle:8.3-jdk17 AS builder

# Set the working directory inside the container
WORKDIR /app

# Copy Gradle project files (only the essentials to optimize caching)
COPY build.gradle settings.gradle /app/

# Copy the Gradle wrapper files
COPY gradlew gradlew.bat /app/
COPY gradle /app/gradle

# Download dependencies (cache this layer)
RUN gradle dependencies --no-daemon

# Copy the rest of the application files
COPY . /app

# Build the application
RUN gradle bootJar --no-daemon

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the Spring Boot jar from the builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Expose the port the app runs on
EXPOSE 8080

# Define the command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
