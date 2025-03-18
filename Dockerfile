# Build stage
FROM gradle:latest AS build
WORKDIR /app

COPY . .

RUN gradle bootJar --no-daemon

# Runtime stage
FROM amazoncorretto:23-alpine-jdk
WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
