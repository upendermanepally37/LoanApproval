# Loan Approval System

An enterprise-grade, microservices-based Loan Approval System designed for banks and financial institutions. The system automates the complete loan lifecycle from application submission to loan disbursement, supporting millions of customers with high availability, scalability, and security.

## 🏗️ Architecture

This system follows **Clean Architecture**, **Domain-Driven Design (DDD)**, **SOLID principles**, and **Microservices Architecture** patterns.

### Technology Stack

- **Java 21** - Latest LTS with modern language features
- **Spring Boot 3.2** - Application framework
- **Spring Cloud 2023** - Microservices patterns (Eureka, Gateway, Config)
- **PostgreSQL 15** - Relational database with ACID compliance
- **Apache Kafka 3.6** - Event streaming for async processing
- **Flyway** - Database migration tool
- **Docker & Kubernetes** - Containerization and orchestration
- **SpringDoc OpenAPI** - API documentation with Swagger UI

### Microservices

```
┌─────────────────────────────────────────────────────────────┐
│                     API Gateway                             │
└────────────────────┬────────────────────────────────────────┘
                     │
    ┌────────────────┼────────────────┬──────────────────────┐
    │                │                │                      │
┌───▼────┐    ┌────▼────┐    ┌─────▼────┐    ┌────────▼─────┐
│Customer │    │ Loan    │    │ Loan     │    │ Eligibility  │
│ Service │    │ Product │    │ App      │    │ Engine      │
│ :8081   │    │ Service │    │ Service  │    │ :8084        │
└─────────┘    │ :8082   │    │ :8083    │    └──────────────┘
               └─────────┘    └──────────┘
```

## 📋 Features

### Customer Management
- Customer registration and profile management
- KYC verification workflow
- Document upload and management
- Risk profiling
- Address and employment details tracking

### Loan Products
- Flexible loan product configuration
- Support for multiple loan types (Personal, Home, Vehicle, Business, Education, Gold)
- Eligibility rule management
- Interest rate and fee configuration
- Product lifecycle management

### Loan Application
- Complete application lifecycle management
- Document upload and verification
- Status tracking and transitions
- Application history and audit trail
- EMI calculation
- Event-driven processing

### Eligibility Engine
- Rule-based eligibility evaluation
- Credit score validation
- Debt-to-income ratio calculation
- Age, salary, and employment checks
- Risk assessment
- Fraud detection

## 🚀 Quick Start

### Prerequisites

- **Java 21** or higher
- **Maven 3.9** or higher
- **Docker** and **Docker Compose**
- **PostgreSQL 15** (or use Docker)
- **Apache Kafka** (or use Docker)

### Using Docker Compose (Recommended)

1. Clone the repository:
```bash
git clone <repository-url>
cd LoanApproval
```

2. Build all services:
```bash
mvn clean package -DskipTests
```

3. Start all services with Docker Compose:
```bash
docker-compose up -d
```

4. Verify services are running:
```bash
docker-compose ps
```

5. Access services:
- Customer Service: http://localhost:8081
- Loan Product Service: http://localhost:8082
- Loan Application Service: http://localhost:8083
- Eligibility Engine: http://localhost:8084
- Eureka Server: http://localhost:8761
- Config Server: http://localhost:8888

### Running Individual Services

Each service can be run independently:

```bash
# Customer Service
cd customer-service
mvn spring-boot:run

# Loan Product Service
cd loan-product-service
mvn spring-boot:run

# Loan Application Service
cd loan-application-service
mvn spring-boot:run

# Eligibility Engine
cd eligibility-engine
mvn spring-boot:run
```

## 📚 API Documentation

Each service provides interactive API documentation via Swagger UI:

- **Customer Service**: http://localhost:8081/api/customers/swagger-ui.html
- **Loan Product Service**: http://localhost:8082/api/loan-products/swagger-ui.html
- **Loan Application Service**: http://localhost:8083/api/loan-applications/swagger-ui.html
- **Eligibility Engine**: http://localhost:8084/api/eligibility/swagger-ui.html

## 🔧 Configuration

### Environment Variables

All services support the following environment variables:

```bash
# Database
DB_URL=jdbc:postgresql://localhost:5432/database
DB_USERNAME=postgres
DB_PASSWORD=postgres

# Service Discovery
EUREKA_SERVER_URL=http://localhost:8761/eureka

# Kafka
KAFKA_SERVERS=localhost:9092

# Server
SERVER_PORT=8081
```

### Application Profiles

- **default**: Local development with embedded configuration
- **docker**: Docker Compose environment
- **kubernetes**: Kubernetes deployment

## 🗄️ Database Setup

### Automatic Migration

Flyway automatically handles database migrations on service startup. Migration scripts are located in:
- `customer-service/src/main/resources/db/migration/`
- `loan-product-service/src/main/resources/db/migration/`
- `loan-application-service/src/main/resources/db/migration/`

### Manual Migration

```bash
mvn flyway:migrate -pl customer-service
mvn flyway:migrate -pl loan-product-service
mvn flyway:migrate -pl loan-application-service
```

## 🧪 Testing

### Run All Tests
```bash
mvn clean test
```

### Run Tests for Specific Module
```bash
mvn test -pl customer-service
mvn test -pl loan-product-service
mvn test -pl loan-application-service
mvn test -pl eligibility-engine
```

### Integration Tests with Testcontainers
Integration tests use Testcontainers for real database testing:
```bash
mvn verify -Pintegration-test
```

## 🚢 Kubernetes Deployment

### Prerequisites
- Kubernetes cluster (minikube, kind, or cloud provider)
- kubectl configured
- Helm 3.x (optional)

### Deploy to Kubernetes

1. Create namespace:
```bash
kubectl apply -f k8s/namespace.yaml
```

2. Deploy databases:
```bash
kubectl apply -f k8s/databases.yaml
```

3. Deploy Kafka:
```bash
kubectl apply -f k8s/kafka.yaml
```

4. Deploy services:
```bash
kubectl apply -f k8s/configmap.yaml
kubectl apply -f k8s/secrets.yaml
kubectl apply -f k8s/customer-service.yaml
kubectl apply -f k8s/loan-product-service.yaml
kubectl apply -f k8s/loan-application-service.yaml
kubectl apply -f k8s/eligibility-engine.yaml
```

5. Verify deployment:
```bash
kubectl get pods -n loan-approval
kubectl get services -n loan-approval
```

### Scale Services
```bash
kubectl scale deployment customer-service --replicas=3 -n loan-approval
```

### View Logs
```bash
kubectl logs -f deployment/customer-service -n loan-approval
```

## 📊 Monitoring

### Health Checks
Each service exposes health endpoints:
- `/actuator/health` - General health
- `/actuator/health/readiness` - Readiness probe
- `/actuator/health/liveness` - Liveness probe

### Metrics
Services expose Prometheus metrics at:
- `/actuator/prometheus`

### Monitoring Stack (Optional)
Deploy Prometheus and Grafana for monitoring:
```bash
kubectl apply -f k8s/monitoring/
```

## 🔒 Security

### Authentication
- JWT token-based authentication
- Token expiration and refresh
- Secure token storage

### Authorization
- Role-Based Access Control (RBAC)
- Fine-grained permissions
- API-level security

### Security Best Practices
- Password encryption with BCrypt
- TLS/HTTPS in production
- Input validation and sanitization
- SQL injection prevention (JPA)
- XSS protection headers
- CORS configuration

## 📈 Performance

### Optimization
- Database connection pooling (HikariCP)
- Query optimization with indexes
- Caching strategies (Caffeine)
- Async processing with Kafka
- Horizontal scaling with Kubernetes HPA

### Performance Tuning
```yaml
# application.yml
spring:
  datasource:
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
  jpa:
    properties:
      hibernate:
        jdbc:
          batch_size: 50
        cache:
          use_second_level_cache: true
```

## 🛠️ Development

### Code Style
- Google Java Style Guide
- Checkstyle and SpotBugs integration
- SonarQube quality gates

### Build and Package
```bash
# Build all modules
mvn clean package

# Skip tests
mvn clean package -DskipTests

# Build specific module
mvn clean package -pl customer-service
```

### IDE Setup
- **IntelliJ IDEA**: Import as Maven project
- **Eclipse**: Import as Maven project
- **VS Code**: Install Java Extension Pack

## 📖 Documentation

- [Architecture Documentation](ARCHITECTURE.md) - Detailed system architecture
- [API Documentation](#api-documentation) - Interactive API docs
- [Database Schema](#database-setup) - Database design
- [Deployment Guide](#kubernetes-deployment) - Deployment instructions

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Development Workflow
1. Create issue for the feature/bug
2. Assign issue to yourself
3. Create branch from main
4. Implement changes with tests
5. Ensure all tests pass
6. Submit pull request
7. Code review and merge

## 📝 License

This project is proprietary software. All rights reserved.

## 👥 Team

- **Solution Architect**: [Name]
- **Tech Lead**: [Name]
- **Development Team**: [Names]

## 📞 Support

For support and questions:
- Email: support@bank.com
- Internal Wiki: [Link]
- Issue Tracker: [Link]

## 🗺️ Roadmap

### Phase 1 (Current) ✅
- [x] Core microservices implementation
- [x] Eligibility engine with rule evaluation
- [x] Event-driven architecture with Kafka
- [x] Docker and Kubernetes deployment
- [x] API documentation

### Phase 2 (Planned)
- [ ] API Gateway implementation
- [ ] OAuth2/JWT security layer
- [ ] Advanced monitoring with Prometheus/Grafana
- [ ] CI/CD pipeline with Jenkins/GitHub Actions
- [ ] Unit and integration test coverage

### Phase 3 (Future)
- [ ] AI/ML integration for credit scoring
- [ ] Digital signatures for documents
- [ ] Mobile applications (iOS/Android)
- [ ] Chatbot for customer support
- [ ] Advanced analytics dashboard

## 🔄 Version History

### Version 1.0.0 (Current)
- Initial release
- Core microservices
- Eligibility engine
- Event-driven architecture
- Docker and Kubernetes support

## 📞 Contact

- **Project Lead**: [Name] - email@bank.com
- **DevOps Team**: devops@bank.com
- **Architecture Team**: architecture@bank.com

---

**Built with ❤️ for modern banking**
