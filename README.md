# Logistic Management System (Backend)

A comprehensive Spring Boot backend application for managing logistics operations, including fleet management, driver tracking, shipment management, and role-based access control.

## Overview

The Logistic Management System is a robust backend service designed to handle complex logistics workflows. It provides APIs for fleet operators to manage their drivers, trucks, and shipments in real-time. The system tracks driver locations, manages shipment statuses, and maintains detailed transaction histories. Built with modern Spring Boot technologies, it ensures security through JWT-based authentication and implements role-based access control.

**Key Problems Solved:**
- Real-time driver location tracking for shipments
- Centralized fleet and driver management across multiple operators
- Secure role-based access control for fleet members
- Comprehensive shipment lifecycle management
- Transaction history and audit trails for fleet operations

## Features

- **JWT Authentication & Authorization** - Secure token-based authentication with role-based access control (DRIVER, ADMIN, MANAGER roles)
- **Fleet Operator Management** - Create, update, and manage multiple fleet operators with member management
- **Driver Management** - Register drivers, track their status (AVAILABLE, ON_TRIP, OFF_DUTY), and manage assignments
- **Truck Management** - Register and manage fleet trucks with assignment to drivers
- **Shipment Management** - Create shipments with pickup/delivery locations, assign trucks and drivers, track status changes
- **Real-time Location Tracking** - Track driver current location (latitude/longitude) during active shipments
- **Shipment Metrics & Analytics** - Generate shipment overview reports and transaction history
- **Role-Based Access Control** - Granular permissions for ADMIN and MANAGER roles within fleet operators
- **Pagination Support** - Efficient data retrieval with configurable page sizes
- **Input Validation** - Comprehensive validation for DTOs including custom UUID validation
- **Global Exception Handling** - Centralized error handling with meaningful error responses
- **CORS Configuration** - Configurable Cross-Origin Resource Sharing for frontend integration
- **Logging & Monitoring** - Google Flogger integration for structured logging and AOP-based logging aspects
- **API Documentation** - OpenAPI/Swagger UI integration for interactive API exploration

## Tech Stack

| Component | Technology | Version |
|-----------|-----------|---------|
| **Language** | Java | 21 |
| **Framework** | Spring Boot | 3.5.7 |
| **Build Tool** | Maven | 3.9.9 |
| **Database** | PostgreSQL | Latest |
| **ORM** | Spring Data JPA / Hibernate | 3.5.7 |
| **Security** | Spring Security + JWT (JJWT) | 0.11.5 |
| **Validation** | Jakarta Validation | 3.5.7 |
| **API Documentation** | SpringDoc OpenAPI | 2.8.1 |
| **Logging** | Google Flogger | 0.8 |
| **AOP** | Spring AOP | 3.5.7 |
| **Mapping** | Lombok (annotations only) | 1.18.36 |
| **Testing** | JUnit 5, Spring Boot Test | 3.5.7 |
| **Container** | Docker | - |

## Project Structure

```
logisticms/
├── src/main/java/com/example/logisticms/
│   ├── LogisticmsApplication.java          # Spring Boot application entry point
│   ├── config/                              # Configuration classes
│   │   ├── SecurityConfig.java              # JWT and Spring Security configuration
│   │   ├── JwtAuthenticationFilter.java     # Custom JWT token validation filter
│   │   ├── JwtAuthEntryPoint.java           # JWT authentication entry point for exceptions
│   │   ├── WebConfig.java                   # CORS and web configuration
│   │   ├── RestClientConfig.java            # REST client configuration
│   │   ├── LoggingAspect.java               # AOP aspect for logging
│   │   └── Beans.java                       # Additional bean definitions
│   ├── controller/                          # REST API endpoints
│   │   ├── FleetOperatorController.java     # Fleet operator CRUD and member management
│   │   ├── DriverController.java            # Driver shipments and location tracking
│   │   ├── ShipmentController.java          # Shipment lifecycle management
│   │   ├── TruckController.java             # Truck management (partial)
│   │   └── TrackingController.java          # Real-time tracking endpoints
│   ├── service/                             # Business logic layer
│   │   ├── impl/                            # Service implementations
│   │   │   ├── FleetOperatorServiceImpl.java    # Fleet operator operations
│   │   │   ├── DriverServiceImpl.java           # Driver operations
│   │   │   ├── ShipmentServiceImpl.java         # Shipment operations
│   │   │   ├── ShipmentMetricsServiceImpl.java  # Analytics and metrics
│   │   │   ├── TruckServiceImpl.java            # Truck operations
│   │   │   ├── TrackingServiceImpl.java         # Location tracking
│   │   │   ├── FleetOperatorRoleServiceImpl.java # Role management
│   │   │   ├── JwtUtil.java                    # JWT utility methods
│   │   │   └── ShipmentStatusUtil.java         # Shipment status utilities
│   │   ├── ShipmentMetricsService.java     # Metrics service interface
│   │   └── client/                         # External service clients
│   ├── repository/                          # Data access layer (JPA repositories)
│   ├── entity/                              # JPA entity models
│   │   ├── BaseEntity.java                  # Base entity with audit fields
│   │   ├── Driver.java                      # Driver entity
│   │   ├── Shipment.java                    # Shipment entity
│   │   ├── FleetOperator.java               # Fleet operator entity
│   │   ├── Truck.java                       # Truck entity
│   │   ├── FleetOperatorMember.java         # Fleet operator member entity
│   │   ├── Tracking.java                    # Tracking/location data entity
│   │   ├── DriverCurrentLocation.java       # Current location snapshot
│   │   ├── DriverStatus.java                # Driver status enum
│   │   ├── ContactDetails.java              # Embedded contact information
│   │   ├── Location.java                    # Embedded location (lat/lon/address)
│   │   └── enums/                           # Entity enumerations
│   ├── dto/                                 # Data Transfer Objects
│   │   ├── ApiResponseDTO.java              # Unified API response wrapper
│   │   ├── DriverDto.java                   # Driver DTO
│   │   ├── ShipmentCreateRequest.java       # Shipment creation request
│   │   ├── ShipmentUpdateRequest.java       # Shipment update request
│   │   ├── ShipmentDetailsResponse.java     # Detailed shipment response
│   │   ├── FleetOperatorDto.java            # Fleet operator DTO
│   │   ├── FleetOperatorMembersResponse.java # Member list response
│   │   ├── TruckDto.java                    # Truck DTO
│   │   ├── TrackingDto.java                 # Location tracking DTO
│   │   ├── JwtClaims.java                   # JWT token claims
│   │   ├── FirebaseLoginRequest.java        # Login request (Firebase auth)
│   │   └── [other DTOs]                     # Additional data transfer objects
│   ├── mapper/                              # Entity-DTO mapping logic
│   │   ├── ShipmentMapper.java              # Shipment entity/DTO mapping
│   │   ├── FleetOperatorMapper.java         # Fleet operator mapping
│   │   └── [other mappers]                  # Additional mappers
│   └── exception/                           # Custom exception classes
│       ├── GlobalExceptionHandler.java      # Centralized exception handling
│       ├── InvalidJwtTokenException.java    # JWT validation errors
│       ├── UnauthorizedOperationException.java # Authorization failures
│       ├── NoResourceFoundException.java    # Resource not found errors
│       ├── RateLimitExceededException.java  # Rate limit errors
│       ├── UserBlockedException.java        # User blocked errors
│       └── LoginMsException.java             # Login service errors
├── src/main/resources/
│   ├── application.properties                # Common Spring Boot properties
│   ├── application-dev.properties            # Development profile config
│   ├── application-prod.properties           # Production profile config
│   ├── static/                               # Static web resources
│   └── templates/                            # Thymeleaf templates
├── src/test/
│   └── java/com/example/logisticms/
│       └── LogisticmsApplicationTests.java  # Integration tests
├── Dockerfile                                # Multi-stage Docker build
├── pom.xml                                   # Maven project configuration
├── mvnw & mvnw.cmd                          # Maven wrapper scripts
└── HELP.md                                   # Project help documentation
```

### Major Package Responsibilities

- **config/** - Spring Security, JWT validation, CORS, AOP logging setup
- **controller/** - RESTful endpoint handlers with request validation
- **service/** - Business logic, data processing, external service integration
- **repository/** - Database operations via Spring Data JPA
- **entity/** - JPA annotated domain models with relationships
- **dto/** - Request/response data structures for API contracts
- **mapper/** - Object mapping between entities and DTOs
- **exception/** - Custom exceptions and global error handling

## Configuration

### Application Properties

The application uses Spring profiles for environment-specific configuration:

| Property | Description | Example Value |
|----------|-------------|----------------|
| `spring.application.name` | Application identifier | logisticms |
| `server.port` | Server port (env variable: PORT) | 8080 |
| `spring.profiles.active` | Active profile (dev/prod) | dev |
| `spring.jpa.database-platform` | JPA dialect | org.hibernate.dialect.PostgreSQLDialect |
| `jwt.expiration` | JWT token expiration (ms) | 86400000 (24 hours) |

### Environment Variables (Required for Production)

| Variable | Description | Required |
|----------|-------------|----------|
| `DATABASE_URL` | PostgreSQL connection URL | Yes (prod) |
| `DATABASE_USERNAME` | Database username | Yes (prod) |
| `DATABASE_PASSWORD` | Database password | Yes (prod) |
| `SPRING_PROFILES_ACTIVE` | Active profile (dev/prod) | No (defaults to dev) |
| `VITE_FRONTEND_URL` | Frontend origin for CORS | Yes |
| `PORT` | Server port | No (defaults to 8080) |

### Configuration Files

**Development** (`application-dev.properties`):
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/logistic_db
spring.datasource.username=postgres
spring.datasource.password=1234
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

**Production** (`application-prod.properties`):
- Uses environment variables for credentials
- Connection pooling: Max 5 connections, Min 1 idle
- Timeout: 30s connection, 10m idle, 30m max lifetime
- SQL logging disabled

## How to Run the Project

### Prerequisites

- Java 21 JDK
- PostgreSQL 12+
- Maven 3.9+
- Docker (optional, for containerized deployment)

### Local Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd logisticms
   ```

2. **Install PostgreSQL and create database**
   ```bash
   # Create database
   createdb logistic_db
   
   # Or using PostgreSQL client
   psql -U postgres -c "CREATE DATABASE logistic_db;"
   ```

3. **Configure local environment**
   - Ensure `application-dev.properties` has correct database credentials
   - Default: `postgresql://localhost:5432/logistic_db`

4. **Build the project**
   ```bash
   # Using Maven wrapper (Windows)
   mvnw.cmd clean package
   
   # Or using Maven wrapper (Linux/Mac)
   ./mvnw clean package
   
   # Or using system Maven
   mvn clean package
   ```

5. **Run the application**
   ```bash
   # Using Maven
   mvnw.cmd spring-boot:run
   
   # Or run the JAR
   java -jar target/logisticms-0.0.1-SNAPSHOT.jar
   ```

6. **Access the application**
   - API Base URL: `http://localhost:8080`
   - API Documentation: `http://localhost:8080/swagger-ui.html`
   - OpenAPI JSON: `http://localhost:8080/v3/api-docs`

### Database Setup

Tables are auto-created via Hibernate DDL (set to `update` mode). On first run:

1. Application starts and creates all entities as tables
2. Relationships are established based on JPA annotations
3. Indexes are created for primary keys

No manual SQL scripts required for development.

### Maven Commands

| Command | Purpose |
|---------|---------|
| `mvn clean` | Remove build artifacts |
| `mvn compile` | Compile source code |
| `mvn test` | Run unit tests |
| `mvn package` | Build JAR artifact |
| `mvn clean package -DskipTests` | Build without running tests |
| `mvn spring-boot:run` | Run application directly |
| `mvn dependency:tree` | Show dependency tree |

## API Documentation

### Base URL
```
http://localhost:8080/api
```

### Authentication
All endpoints (except public docs) require JWT token in the `Authorization` header:
```
Authorization: Bearer <jwt_token>
```

### Endpoint Summary

#### Fleet Operator Endpoints

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| `POST` | `/api/fleetoperators` | Create new fleet operator | User must be authenticated |
| `PUT` | `/api/fleetoperators/{id}` | Update fleet operator (Admin only) | ADMIN |
| `GET` | `/api/fleetoperators` | List all fleet operators for user | Authenticated |
| `GET` | `/api/fleetoperators/members/{id}` | Get members of fleet operator | Member |
| `POST` | `/api/fleetoperators/members/{id}` | Add/update fleet member | ADMIN |
| `POST` | `/api/fleetoperators/{id}/trucks` | Create truck for fleet | ADMIN |
| `PUT` | `/api/fleetoperators/{id}/trucks/{truckId}` | Update truck details | ADMIN |

#### Driver Endpoints

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| `GET` | `/api/v1/drivers/{id}` | Get driver profile by ID | Authenticated |
| `GET` | `/api/v1/drivers/shipments` | Get all shipments assigned to driver | DRIVER |
| `GET` | `/api/v1/drivers/fleetOperators` | Get fleet operators for driver | DRIVER |
| `GET` | `/api/v1/drivers/fleetOperators/{id}/profile` | Get driver profile for specific fleet | DRIVER |
| `GET` | `/api/v1/drivers/shipments/{id}/tracking` | Get tracking details for shipment | DRIVER |
| `PUT` | `/api/v1/drivers/shipment/{id}/location` | Update current driver location | DRIVER |

#### Shipment Endpoints

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| `POST` | `/api/v1/fleetoperators/{id}/shipments` | Create shipment | ADMIN |
| `PUT` | `/api/v1/shipments/{id}` | Assign trucks/drivers to shipment | Authenticated |
| `GET` | `/api/v1/fleetoperators/{id}/shipments` | Get all shipments for fleet | Member |
| `GET` | `/api/v1/shipments/{id}` | Get shipment details | Authenticated |
| `PUT` | `/api/v1/shipments/{id}/status` | Update shipment status | Authenticated |
| `GET` | `/api/v1/shipments/driver-locations` | Get current driver locations | Authenticated |
| `GET` | `/api/v1/fleetoperators/{id}/shipments/overview` | Get shipment overview stats | Member |
| `GET` | `/api/v1/fleetoperators/{id}/shipments/transaction-history` | Get transaction history (paginated) | Member |

### Sample Request/Response Bodies

#### Create Fleet Operator
**Request:**
```json
POST /api/fleetoperators
Content-Type: application/json
Authorization: Bearer <token>

{
  "name": "Express Logistics Inc",
  "gstNumber": "27AABCU9603R1Z0",
  "contactPhone": "+91-9876543210",
  "contactEmail": "contact@expresslogistics.com",
  "address": "123 Business Park, Tech City, TC 12345",
  "description": "Premier logistics provider specializing in nationwide express delivery"
}
```

**Response (201 Created):**
```json
{
  "success": true,
  "message": "Fleet Operator created successfully",
  "data": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "name": "Express Logistics Inc",
    "gstNumber": "27AABCU9603R1Z0",
    "contactPhone": "+91-9876543210",
    "contactEmail": "contact@expresslogistics.com",
    "address": "123 Business Park, Tech City, TC 12345",
    "description": "Premier logistics provider specializing in nationwide express delivery"
  }
}
```

#### Create Shipment
**Request:**
```json
POST /api/v1/fleetoperators/{fleetOperatorId}/shipments
Content-Type: application/json
Authorization: Bearer <token>

{
  "shipmentName": "Electronics Delivery",
  "shipmentFormalName": "High-value Electronics Shipment #2024-001",
  "pickupDate": "2024-01-20T10:00:00",
  "pickupLocation": {
    "latitude": 28.6139,
    "longitude": 77.2090,
    "address": "123 Warehouse Lane, Delhi"
  },
  "deliveryLocation": {
    "latitude": 19.0760,
    "longitude": 72.8777,
    "address": "456 Distribution Center, Mumbai"
  },
  "shipmentWeight": 250.50,
  "shipmentTotalEstimatedCost": 5000.00,
  "shipmentSpecialInstructions": "Handle with care - fragile electronics. Requires signature on delivery."
}
```

**Response (201 Created):**
```json
{
  "success": true,
  "message": "Shipment created successfully",
  "data": {
    "id": "660e8400-e29b-41d4-a716-446655440001",
    "shipmentName": "Electronics Delivery",
    "shipmentStatus": "PENDING",
    "pickupDate": "2024-01-20T10:00:00",
    "shipmentWeight": 250.50,
    "shipmentTotalEstimatedCost": 5000.00
  }
}
```

#### Update Driver Location
**Request:**
```json
PUT /api/v1/drivers/shipment/{shipmentId}/location
Content-Type: application/json
Authorization: Bearer <token>

{
  "latitude": 28.6245,
  "longitude": 77.1920
}
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Driver location updated successfully",
  "data": null
}
```

#### Get Shipment Details
**Response (200 OK):**
```json
{
  "success": true,
  "message": "Shipment summary fetched successfully",
  "data": {
    "id": "660e8400-e29b-41d4-a716-446655440001",
    "shipmentName": "Electronics Delivery",
    "shipmentFormalName": "High-value Electronics Shipment #2024-001",
    "pickupDate": "2024-01-20T10:00:00",
    "pickupLocation": {
      "latitude": 28.6139,
      "longitude": 77.2090,
      "address": "123 Warehouse Lane, Delhi"
    },
    "deliveryLocation": {
      "latitude": 19.0760,
      "longitude": 72.8777,
      "address": "456 Distribution Center, Mumbai"
    },
    "shipmentWeight": 250.50,
    "shipmentTotalEstimatedCost": 5000.00,
    "shipmentStatus": "IN_TRANSIT",
    "shipmentSpecialInstructions": "Handle with care - fragile electronics. Requires signature on delivery."
  }
}
```

## Security

### Authentication & Authorization

**JWT-Based Authentication:**
- Tokens are validated on every request via `JwtAuthenticationFilter`
- Tokens must be included in the `Authorization: Bearer <token>` header
- Invalid or expired tokens trigger `InvalidJwtTokenException`
- Token expiration: 24 hours (86400000 ms, configurable via `jwt.expiration`)

**Security Configuration:**
```java
- CSRF protection: Disabled (for stateless JWT-based API)
- CORS: Configured via environment variable VITE_FRONTEND_URL
- Public endpoints: API docs (/v3/api-docs/**), Swagger UI, OPTIONS requests
- All other endpoints: Require authentication
```

**Role-Based Access Control (RBAC):**

| Role | Permissions | Examples |
|------|------------|----------|
| **ADMIN** | Full control over fleet operator<br>Create/update shipments<br>Manage members and trucks<br>View all data | Create fleets, assign drivers, manage roles |
| **MANAGER** | View shipments<br>Limited member management<br>Cannot create fleet operations | View reports, approve shipments |
| **DRIVER** | View assigned shipments<br>Update location<br>View tracking info | Update location, view assigned loads |

**Authorization Checks:**
- `@PreAuthorize` annotations on controller methods
- Manual checks in service layer for complex business logic
- `UnauthorizedOperationException` thrown for violations

### Example Security Implementation
```java
@PreAuthorize("hasRole('DRIVER')")
@PutMapping("/shipment/{shipmentId}/location")
public ApiResponseDTO<Void> updateDriverLocation(
    @RequestBody DriverLocationUpdateRequest request,
    @PathVariable UUID shipmentId) {
    // Only DRIVER role can access
    String phoneNumber = SecurityContextHolder.getContext()
        .getAuthentication().getPrincipal().toString();
    driverService.updateDriverLocation(shipmentId, phoneNumber, request);
    return ApiResponseDTO.<Void>builder()
        .success(true)
        .message("Driver location updated successfully")
        .build();
}
```

### CORS Configuration
- Allows requests from frontend URL specified in `VITE_FRONTEND_URL`
- Supports: GET, POST, PUT, DELETE, OPTIONS
- Allows all headers and credentials

## Error Handling & Validation

### Global Exception Handler
All exceptions are centrally handled by `GlobalExceptionHandler` which returns consistent error responses:

```json
{
  "success": false,
  "message": "Error description",
  "data": null
}
```

### Custom Exceptions

| Exception | Status Code | Use Case |
|-----------|------------|----------|
| `InvalidJwtTokenException` | 401 | JWT token validation fails |
| `UnauthorizedOperationException` | 403 | User lacks required permissions |
| `NoResourceFoundException` | 404 | Requested resource doesn't exist |
| `RateLimitExceededException` | 429 | API rate limit exceeded |
| `UserBlockedException` | 423 | User account is blocked |
| `LoginMsException` | 400 | Authentication service error |

### Input Validation
- Uses `jakarta.validation` annotations on DTOs
- Custom validators for UUID format validation
- Field-level validation:
  - Required fields: `@NotNull`, `@NotBlank`
  - Number constraints: `@Positive`, `@PositiveOrZero`
  - String length: `@Size(max=...)`
  - Custom patterns: `@Pattern(regexp=...)`

**Example DTO Validation:**
```java
@PostMapping
public ApiResponseDTO<FleetOperatorDto> createFleetOperator(
    @RequestBody @Valid FleetOperatorDto fleetOperatorDto) {
    // Validation happens automatically via @Valid
    // Returns 400 Bad Request if validation fails
}
```

## Testing

### Test Framework
- **JUnit 5** (Jupiter) for unit testing
- **Spring Boot Test** for integration testing
- **MockMvc** for controller testing

### Test File Location
```
src/test/java/com/example/logisticms/
└── LogisticmsApplicationTests.java
```

### Running Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=LogisticmsApplicationTests

# Run with coverage
mvn test jacoco:report

# Run without building
mvn test -DskipBuild=true
```

### Test Coverage
- Controllers: Request/response validation, authorization checks
- Services: Business logic, data transformation
- Repositories: Database operations (via Spring Data JPA)

## Build & Deployment

### Building JAR

```bash
# Build optimized JAR (skips tests)
mvn clean package -DskipTests

# Build with tests
mvn clean package

# Output: target/logisticms-0.0.1-SNAPSHOT.jar
```

### Running JAR

```bash
# With default settings
java -jar target/logisticms-0.0.1-SNAPSHOT.jar

# With environment variables
java -jar target/logisticms-0.0.1-SNAPSHOT.jar \
  --spring.profiles.active=prod \
  --server.port=8081

# With custom database
java -Dspring.datasource.url=jdbc:postgresql://db-host:5432/logistic_db \
  -Dspring.datasource.username=user \
  -Dspring.datasource.password=pass \
  -jar target/logisticms-0.0.1-SNAPSHOT.jar
```

### Docker Deployment

**Build Docker Image:**
```bash
docker build -t logisticms:latest .
```

**Run Docker Container:**
```bash
docker run -d \
  --name logisticms \
  -p 8081:8080 \
  -e DATABASE_URL=jdbc:postgresql://postgres-container:5432/logistic_db \
  -e DATABASE_USERNAME=postgres \
  -e DATABASE_PASSWORD=secure_password \
  -e VITE_FRONTEND_URL=http://localhost:3000 \
  -e SPRING_PROFILES_ACTIVE=prod \
  logisticms:latest
```

**Docker Compose (recommended for local development):**
```yaml
version: '3.8'
services:
  postgres:
    image: postgres:15-alpine
    environment:
      POSTGRES_DB: logistic_db
      POSTGRES_PASSWORD: 1234
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data

  logisticms:
    build: .
    ports:
      - "8080:8080"
    environment:
      DATABASE_URL: jdbc:postgresql://postgres:5432/logistic_db
      DATABASE_USERNAME: postgres
      DATABASE_PASSWORD: 1234
      VITE_FRONTEND_URL: http://localhost:3000
      SPRING_PROFILES_ACTIVE: dev
    depends_on:
      - postgres

volumes:
  postgres_data:
```

**Dockerfile Details:**
- **Build Stage**: Uses Maven 3.9.9 with Java 21 to compile and package
- **Runtime Stage**: Uses Eclipse Temurin 21 JRE for minimal image size
- **Exposed Port**: 8081
- **Entry Point**: Runs the JAR with Java

## Logging & Monitoring

### Logging Framework
- **Google Flogger** for structured logging
- **Spring AOP** aspect (`LoggingAspect`) for method-level logging
- Logs method execution, parameters, and results

### Log Configuration

**Development:**
```properties
spring.jpa.show-sql=true
```

**Production:**
```properties
spring.jpa.show-sql=false
```

### Available Logs
- Request/response logging via AOP
- JPA SQL query logging (dev only)
- Security authentication logs
- Exception stack traces

### Monitoring Metrics
- **Datasource Metrics**: Connection pool status
- **HTTP Metrics**: Request count and latency
- **JVM Metrics**: Memory, garbage collection

## Future Enhancements

Based on the current architecture, consider these improvements:

1. **Advanced Analytics**
   - Shipment delivery time optimization
   - Driver performance metrics
   - Cost analysis per shipment

2. **Scalability Improvements**
   - Caching layer (Redis) for frequently accessed data
   - Message queue (RabbitMQ/Kafka) for async shipment processing
   - Elasticsearch for advanced search capabilities

3. **Real-time Features**
   - WebSocket support for live location tracking
   - Push notifications for shipment status updates
   - Real-time dashboard updates

4. **Enhanced Security**
   - OAuth2 / OpenID Connect integration
   - Two-factor authentication
   - API key rotation mechanism
   - Request signing for sensitive operations

5. **Operational Features**
   - Automated route optimization
   - Fuel consumption tracking
   - Maintenance scheduling
   - Driver compliance monitoring
   - Insurance and documentation management

6. **API Improvements**
   - GraphQL support alongside REST
   - API versioning strategy
   - Rate limiting with configurable quotas
   - Webhook support for external integrations

7. **Data Management**
   - Audit logging for compliance
   - Data archival strategy
   - GDPR compliance features
   - Backup and disaster recovery

8. **Testing**
   - Comprehensive integration test suite
   - Performance load testing
   - Security penetration testing
   - Contract testing with frontend

## Contribution Guidelines

1. **Code Style**
   - Follow Google Java Style Guide
   - Use Lombok for boilerplate reduction
   - Keep methods focused and testable

2. **Naming Conventions**
   - Controllers: `*Controller` suffix
   - Services: `*ServiceImpl` for implementations
   - DTOs: `*Dto`, `*Request`, `*Response` suffixes
   - Entities: Use singular names (Driver, not Drivers)

3. **Pull Request Process**
   - Create feature branch from `main`
   - Include unit tests for new features
   - Update README for API changes
   - Ensure all tests pass locally

4. **Commit Messages**
   - Use present tense: "Add feature" not "Added feature"
   - Be descriptive: "Add JWT validation filter" not "Fix bug"
   - Reference issues: "Closes #123"

5. **Documentation**
   - Document public APIs
   - Add JavaDoc for complex business logic
   - Update configuration docs for property changes

## License

This project is licensed under the MIT License - see the LICENSE file for details.

---

**Last Updated:** January 19, 2026  
**Version:** 0.0.1-SNAPSHOT  
**Java Version:** 21  
**Spring Boot Version:** 3.5.7
