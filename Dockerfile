FROM gradle:7.6.1-jdk17 AS builder
WORKDIR /app
COPY . .
RUN gradle bootJar --no-daemon

FROM eclipse-temurin:17-jre-focal
WORKDIR /app
COPY --from=builder /app/build/libs/app.jar .
EXPOSE 8080
CMD ["java", "-Dserver.port=$PORT", "-jar", "app.jar"]