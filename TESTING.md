# Loan Approval System - Testing Guide

## Overview

This guide provides instructions for testing the Loan Approval System. Since the system is a microservices architecture, testing requires setting up the infrastructure (databases, message broker) and running the services.

## Prerequisites

- Java 21
- Maven 3.9+
- PostgreSQL 15 (or Docker)
- Apache Kafka 3.6 (or Docker)
- API testing tool (Postman, curl, or similar)

## Build Status

✅ **Build successful** - All modules compile successfully:
- Common Module
- Domain Module
- Customer Service
- Loan Product Service
- Loan Application Service
- Eligibility Engine

## Infrastructure Setup

### Option 1: Using Docker Compose (Recommended)

If you have Docker installed, run:
```bash
docker-compose up -d
```

This will start:
- 3 PostgreSQL databases (customerdb, loanproductdb, loanapplicationdb)
- Apache Kafka and Zookeeper
- All microservices

### Option 2: Manual Setup

#### PostgreSQL Setup

Create three databases:
```sql
CREATE DATABASE customerdb;
CREATE DATABASE loanproductdb;
CREATE DATABASE loanapplicationdb;
```

#### Kafka Setup

Download and start Apache Kafka:
```bash
# Download Kafka
wget https://downloads.apache.org/kafka/3.6.1/kafka_2.13-3.6.1.tgz
tar -xzf kafka_2.13-3.6.1.tgz
cd kafka_2.13-3.6.1

# Start Zookeeper
bin/zookeeper-server-start.sh config/zookeeper.properties

# Start Kafka (in another terminal)
bin/kafka-server-start.sh config/server.properties
```

## Running Services

### Run Services Individually

Each service can be run independently with Maven:

```bash
# Customer Service (Port 8081)
cd customer-service
mvn spring-boot:run

# Loan Product Service (Port 8082)
cd loan-product-service
mvn spring-boot:run

# Loan Application Service (Port 8083)
cd loan-application-service
mvn spring-boot:run

# Eligibility Engine (Port 8084)
cd eligibility-engine
mvn spring-boot:run
```

### Service Configuration

Each service requires the following environment variables:

```bash
# Database Configuration
DB_URL=jdbc:postgresql://localhost:5432/database_name
DB_USERNAME=postgres
DB_PASSWORD=postgres

# Service Discovery (optional for local testing)
EUREKA_SERVER_URL=http://localhost:8761/eureka

# Kafka (optional for local testing)
KAFKA_SERVERS=localhost:9092
```

## API Testing

### Swagger UI Access

Each service provides interactive API documentation:

- **Customer Service**: http://localhost:8081/api/customers/swagger-ui.html
- **Loan Product Service**: http://localhost:8082/api/loan-products/swagger-ui.html
- **Loan Application Service**: http://localhost:8083/api/loan-applications/swagger-ui.html
- **Eligibility Engine**: http://localhost:8084/api/eligibility/swagger-ui.html

### Test Scenarios

#### 1. Customer Service Testing

**Create a Customer:**
```bash
POST http://localhost:8081/api/customers
Content-Type: application/json

{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "phoneNumber": "+1234567890",
  "dateOfBirth": "1990-01-01",
  "gender": "MALE",
  "maritalStatus": "SINGLE",
  "panNumber": "ABCDE1234F",
  "aadhaarNumber": "1234-5678-9012",
  "addresses": [
    {
      "addressType": "PERMANENT",
      "street": "123 Main St",
      "city": "New York",
      "state": "NY",
      "postalCode": "10001",
      "country": "USA"
    }
  ],
  "employmentDetails": [
    {
      "employmentType": "FULL_TIME",
      "employerName": "Tech Corp",
      "monthlyIncome": 5000.00,
      "annualIncome": 60000.00,
      "employmentStartDate": "2020-01-01",
      "currentEmployment": true,
      "industry": "Technology"
    }
  ]
}
```

**Get Customer by Number:**
```bash
GET http://localhost:8081/api/customers/CUST123456
```

**Update KYC Status:**
```bash
PATCH http://localhost:8081/api/customers/CUST123456/kyc
Content-Type: application/json

{
  "kycStatus": "VERIFIED",
  "remarks": "KYC documents verified successfully"
}
```

#### 2. Loan Product Service Testing

**Create a Loan Product:**
```bash
POST http://localhost:8082/api/loan-products
Content-Type: application/json

{
  "productCode": "PL001",
  "productName": "Personal Loan",
  "loanType": "PERSONAL",
  "interestRate": 12.5,
  "processingFee": 500.00,
  "processingFeePercentage": 1.0,
  "minTenureMonths": 12,
  "maxTenureMonths": 60,
  "minLoanAmount": 10000.00,
  "maxLoanAmount": 500000.00,
  "minMonthlySalary": 2000.00,
  "minCreditScore": 650,
  "minAge": 21,
  "maxAge": 60,
  "debtToIncomeRatio": 50.0,
  "active": true
}
```

**Get Active Products:**
```bash
GET http://localhost:8082/api/loan-products/active
```

**Get Product by Type:**
```bash
GET http://localhost:8082/api/loan-products/type/PERSONAL
```

#### 3. Loan Application Service Testing

**Create a Loan Application:**
```bash
POST http://localhost:8083/api/loan-applications
Content-Type: application/json

{
  "customerId": 1,
  "loanProductId": 1,
  "requestedAmount": 50000.00,
  "requestedTenureMonths": 36,
  "remarks": "Home renovation loan"
}
```

**Submit Application:**
```bash
POST http://localhost:8083/api/loan-applications/LA1234567890/submit
```

**Get Application Status:**
```bash
GET http://localhost:8083/api/loan-applications/LA1234567890
```

**Update Application Status:**
```bash
PATCH http://localhost:8083/api/loan-applications/LA1234567890/status
Content-Type: application/json

{
  "status": "APPROVED",
  "remarks": "Application approved after verification"
}
```

**Upload Document:**
```bash
POST http://localhost:8083/api/loan-applications/LA1234567890/documents
Content-Type: application/json

{
  "documentType": "INCOME_PROOF",
  "documentName": "salary_slip_jan.pdf",
  "documentPath": "/documents/salary_slip_jan.pdf",
  "documentNumber": "DOC001",
  "documentFormat": "PDF",
  "fileSize": 102400
}
```

#### 4. Eligibility Engine Testing

**Evaluate Eligibility:**
```bash
POST http://localhost:8084/api/eligibility/evaluate
Content-Type: application/json

{
  "customerId": 1,
  "loanProductId": 1,
  "requestedAmount": 50000.00,
  "requestedTenureMonths": 36
}
```

**Expected Response:**
```json
{
  "eligibilityStatus": "ELIGIBLE",
  "summary": "Application eligible - all checks passed",
  "maxEligibleAmount": 500000.00,
  "maxEligibleTenure": 60,
  "recommendedInterestRate": 12.5,
  "estimatedEmi": 1675.00,
  "ruleResults": [
    {
      "ruleName": "AGE_CHECK",
      "ruleType": "AGE",
      "description": "Customer age must be within the product's age range",
      "passed": true,
      "message": "Age is within acceptable range",
      "actualValue": 34,
      "requiredValue": "21-60",
      "mandatory": true
    }
    // ... more rule results
  ],
  "totalRules": 7,
  "passedRules": 7,
  "failedRules": 0,
  "hasFraudFlags": false
}
```

## Integration Testing

### Testcontainers Setup

The project is configured for integration testing with Testcontainers. To run integration tests:

```bash
mvn verify -Pintegration-test
```

This will:
- Start PostgreSQL containers automatically
- Run integration tests with real databases
- Clean up containers after tests complete

### Manual Integration Test Flow

1. **Create Customer**
2. **Update KYC to VERIFIED**
3. **Create Loan Product**
4. **Evaluate Eligibility** (using Customer ID and Loan Product ID)
5. **Create Loan Application**
6. **Submit Application**
7. **Upload Documents**
8. **Update Status to APPROVED**

## Health Checks

Each service exposes health endpoints:

```bash
# General health
GET http://localhost:8081/actuator/health
GET http://localhost:8082/actuator/health
GET http://localhost:8083/actuator/health
GET http://localhost:8084/actuator/health

# Readiness probe
GET http://localhost:8081/actuator/health/readiness

# Liveness probe
GET http://localhost:8081/actuator/health/liveness
```

## Troubleshooting

### Common Issues

**Service fails to start due to database connection:**
- Verify PostgreSQL is running
- Check database credentials in application.yml
- Ensure database exists

**Kafka connection errors:**
- Verify Kafka is running on port 9092
- Check KAFKA_SERVERS environment variable
- Kafka is optional for basic functionality

**Port conflicts:**
- Change server port in application.yml
- Check for other services using the same ports

### Logs

View service logs for debugging:
```bash
# For Maven
mvn spring-boot:run

# For Docker
docker logs <container-name>

# For Kubernetes
kubectl logs -f deployment/<service-name> -n loan-approval
```

## Performance Testing

### Load Testing with JMeter

Create a JMeter test plan with:
- Thread groups for each service
- HTTP request samplers for API endpoints
- Assertions for response validation
- Listeners for results

### Sample Load Test Scenarios

1. **Customer Registration Load Test**
   - 100 concurrent users
   - Create 1000 customers in 1 minute
   
2. **Loan Application Load Test**
   - 50 concurrent users
   - Submit 500 applications in 1 minute

3. **Eligibility Evaluation Load Test**
   - 100 concurrent evaluations
   - 1000 evaluations in 1 minute

## Security Testing

### Authentication Testing (Future)

Once security is implemented:
```bash
# Login to get JWT token
POST http://localhost:8081/api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "password"
}

# Use token in subsequent requests
GET http://localhost:8081/api/customers
Authorization: Bearer <jwt-token>
```

### Input Validation Testing

Test for:
- SQL injection attempts
- XSS attacks
- Invalid data types
- Boundary value violations

## Test Data

### Sample Customer Data

```json
{
  "firstName": "Jane",
  "lastName": "Smith",
  "email": "jane.smith@example.com",
  "phoneNumber": "+1987654321",
  "dateOfBirth": "1985-05-15",
  "gender": "FEMALE",
  "maritalStatus": "MARRIED",
  "panNumber": "FGHIJ5678K",
  "aadhaarNumber": "9876-5432-1098"
}
```

### Sample Loan Product Data

```json
{
  "productCode": "HL001",
  "productName": "Home Loan",
  "loanType": "HOME",
  "interestRate": 8.5,
  "processingFee": 1000.00,
  "processingFeePercentage": 0.5,
  "minTenureMonths": 60,
  "maxTenureMonths": 240,
  "minLoanAmount": 100000.00,
  "maxLoanAmount": 5000000.00,
  "minMonthlySalary": 3000.00,
  "minCreditScore": 700,
  "minAge": 25,
  "maxAge": 55,
  "debtToIncomeRatio": 40.0
}
```

## Next Steps

1. **Set up infrastructure** (PostgreSQL, Kafka)
2. **Run services** using Maven or Docker
3. **Test APIs** using Swagger UI or Postman
4. **Verify integration** between services
5. **Run integration tests** with Testcontainers
6. **Performance testing** with JMeter or Gatling

## Support

For issues or questions:
- Check application logs
- Verify service health endpoints
- Review API documentation in Swagger UI
- Check database connection and schema

---

**Note**: Full end-to-end testing requires all services to be running with proper database and Kafka infrastructure.
