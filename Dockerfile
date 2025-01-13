FROM openjdk:17-jdk-slim AS builder

WORKDIR /app

COPY gradlew ./
COPY gradle ./gradle
COPY build.gradle settings.gradle ./

RUN chmod +x gradlew

# Add this to debug gradle build
RUN ./gradlew --version

COPY src ./src

RUN ./gradlew bootJar

# Add these debug commands
RUN echo "Listing build/libs directory:"
RUN ls -la /app/build/libs/
RUN echo "Checking JAR content:"
RUN jar tvf /app/build/libs/*.jar | grep -i cashly

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

# Add this to verify the final JAR
RUN echo "Verifying final JAR content:"
RUN jar tvf app.jar | grep -i cashly

EXPOSE 8080

# Modify the CMD to print classloader information
CMD ["java", "-verbose:class", "-jar", "app.jar"]