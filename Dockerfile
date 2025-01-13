FROM eclipse-temurin:17-jdk-alpine as builder

WORKDIR /app

COPY gradlew ./
COPY gradle ./gradle
COPY build.gradle settings.gradle ./

RUN chmod +x gradlew

RUN ./gradlew dependencies

COPY src ./src

RUN ./gradlew bootJar

FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
