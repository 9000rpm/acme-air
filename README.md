# ACME Air - Flight and Booking APIs

## Run application and tests
- This runs the tests and then starts up the application`./gradlew clean test build bootRun`
- Uses OpenAPI/Swagger endpoint to test APIs `http://localhost:8080/swagger-ui/index.html`
- Uses Spring Actuators for health monitoring: `http://localhost:8080/actuator/health`
- Sample test data loaded can be found in src\main\resources\flights.json

## Assumptions
- All backend APIs and database tables will use UTC for dates. 
- Frontend UI will handle the display of information using time zones.
- Authentication not required, in production will consider OAuth2, JWT, Basic Auth, CSRF token, API Keys.
- Database not required, in production will consider using JPA and PostgreSQL.
- Docker image not required, in production will consider packaging as an image to deploy on Kubernetes.
- API/Data auditing not required, in production will consider using tables or adding columns to track when, how and who modified the data. APIs can be audited using the web server/API Gateway logs.

## Requirements met
- Application use Spring Boot 3, Gradle, Java 21.
- Developed all 5 required APIs.
- 2 functional tests per API (success and failure).
- Unit tests for the creation and cancelling of bookings.

## CI/CD automated deployment
- Automate testing and deployment using CI/CD pipeline using tools CodePipeline, Jenkins, Github Actions.
- Testing - Unit, integration, and end-to-end testing.
- CI Pipeline - Run tests, code checks, and build artifacts.
- CD Pipeline - Automate deployment to environments (dev/stage/prod), use Spring Profile.
- Infrastructure - Use containers (Docker), IaC (Terraform, Ansible), Kubernetes.
- Monitoring - Post-deploy checks, alerts, Grafana.

## Future Enhancements
- Use API versioning of the URL (/api/v1/flights) or header versioning (Accept: application/vnd.myapp.v1+json).
- Use caching strategy for GET APIs called frequently. E.g. Redis
- Use Flyway for database versioning.
- Use authentication like OAuth2, JWT, Basic Auth, CSRF token, API Keys.
- Use HTTPS to secure data between client and server

## Future Microservices Enhancements
- Use API Gateway for routing, rate limiting, load balancing.
- Use Kubernetes to easily scale.
- Use protocol buffers and gRPC for secure, fast and lightweight communication between microservice.
- Use Kafka for event driven architecture.
- Use GrpahQL for BFF APIs, lightweight, maintainable, clean.
