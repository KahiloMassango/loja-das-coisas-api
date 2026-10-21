# ==========================================
# 1. Build Stage
# ==========================================
FROM gradle:8.11-jdk21-alpine AS build
WORKDIR /app

# Cache Gradle dependencies by copying wrapper & config first
COPY gradlew settings.gradle.kts build.gradle.kts ./
COPY gradle ./gradle
RUN chmod +x gradlew && ./gradlew dependencies --no-daemon

# Copy source files and build executable JAR
COPY src ./src
RUN ./gradlew bootJar -x test --no-daemon

# ==========================================
# 2. Runtime Stage
# ==========================================
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Install wget for Dokploy / Docker health check
RUN apk add --no-cache wget

# Create persistent storage folder for uploads
RUN mkdir -p /app/uploads

# Copy built JAR from builder
COPY --from=build /app/build/libs/*.jar app.jar

# Defaults for production in Dokploy
ENV SPRING_PROFILES_ACTIVE=prod
ENV PORT=8080

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=5s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]
