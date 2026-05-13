# Build Stage - Optimized for layer caching
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy only pom.xml first to cache dependencies
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw mvnw.cmd ./

# Download dependencies (this layer is cached unless pom.xml changes)
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build application
RUN mvn clean package -q -DskipTests

# Runtime Stage - Minimal JRE Alpine for smaller image size
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Add non-root user for security
RUN addgroup -S appuser && adduser -S appuser -G appuser

# Copy jar from build stage
COPY --from=build /app/target/*.jar app.jar

# Change ownership
RUN chown -R appuser:appuser /app

# Switch to non-root user
USER appuser

# Expose port
EXPOSE 8081

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
    CMD wget -q -O- http://localhost:8081/actuator/health || exit 1

# Run application with optimized JVM settings for container
ENTRYPOINT ["java", "-XX:+UseG1GC", "-XX:MaxGCPauseMillis=200", "-XX:InitiatingHeapOccupancyPercent=35", "-jar", "app.jar"]
