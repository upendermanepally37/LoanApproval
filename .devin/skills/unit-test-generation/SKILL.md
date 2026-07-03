---
name: unit-test-generation
description: Generate comprehensive unit tests for Spring Boot microservices using JUnit 5, Mockito, and Testcontainers
---

# Unit Test Generation Skill

This skill provides instructions for generating comprehensive unit tests for the Loan Approval System microservices.

## When to Use This Skill

Use this skill when you need to:
- Create unit tests for service classes
- Create unit tests for repository classes
- Create unit tests for controller classes
- Create unit tests for mapper classes
- Create integration tests with Testcontainers

## Technology Stack

- **JUnit 5** - Testing framework
- **Mockito** - Mocking framework
- **Testcontainers** - Integration testing with real containers
- **Spring Boot Test** - Spring testing support
- **AssertJ** - Fluent assertions

## Test Structure

Follow this structure for test classes:

```
src/test/java/com/bank/loanapproval/<service>/
├── controller/
│   └── <Controller>Test.java
├── service/
│   └── <Service>Test.java
├── repository/
│   └── <Repository>Test.java
├── mapper/
│   └── <Mapper>Test.java
└── integration/
    └── <Integration>Test.java
```

## Test Naming Conventions

- Unit tests: `<ClassName>Test.java`
- Integration tests: `<ClassName>IntegrationTest.java`

## Test Templates

### Service Test Template

```java
package com.bank.loanapproval.<service>.service;

import com.bank.loanapproval.<service>.dto.*;
import com.bank.loanapproval.<service>.mapper.<Mapper>;
import com.bank.loanapproval.<service>.repository.<Repository>;
import com.bank.loanapproval.common.exception.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class <Service>Test {

    @Mock
    private <Repository> <repository>;

    @Mock
    private <Mapper> <mapper>;

    @InjectMocks
    private <Service> <service>;

    private <Entity> testEntity;
    private <Request> testRequest;

    @BeforeEach
    void setUp() {
        testEntity = createTestEntity();
        testRequest = createTestRequest();
    }

    @Test
    void should<TestMethod>() {
        // Given
        when(<repository>.<method>(any())).thenReturn(testEntity);

        // When
        <Result> result = <service>.<method>(testRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.<field>).isEqualTo(<expected>);
        verify(<repository>).<method>(any());
    }

    @Test
    void shouldThrowExceptionWhen<Condition>() {
        // Given
        when(<repository>.<method>(any())).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> <service>.<method>(<input>))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining(<message>);
    }

    private <Entity> createTestEntity() {
        return <Entity>.builder()
            .<field>(<value>)
            .build();
    }

    private <Request> createTestRequest() {
        return <Request>.builder()
            .<field>(<value>)
            .build();
    }
}
```

### Controller Test Template

```java
package com.bank.loanapproval.<service>.controller;

import com.bank.loanapproval.<service>.service.<Service>;
import com.bank.loanapproval.<service>.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(<Controller>.class)
class <Controller>Test {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private <Service> <service>;

    private <Response> testResponse;

    @BeforeEach
    void setUp() {
        testResponse = createTestResponse();
    }

    @Test
    void should<TestMethod>() throws Exception {
        // Given
        when(<service>.<method>(any())).thenReturn(testResponse);

        // When & Then
        mockMvc.perform(post("/api/<endpoint>")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testRequest)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.<field>").value(<expected>));
    }

    private <Response> createTestResponse() {
        return <Response>.builder()
            .<field>(<value>)
            .build();
    }
}
```

### Repository Test Template

```java
package com.bank.loanapproval.<service>.repository;

import com.bank.loanapproval.<service>.entity.<Entity>;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class <Repository>Test {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private <Repository> <repository>;

    private <Entity> testEntity;

    @BeforeEach
    void setUp() {
        testEntity = createTestEntity();
    }

    @Test
    void shouldSaveEntity() {
        // When
        <Entity> saved = <repository>.save(testEntity);

        // Then
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
    }

    @Test
    void shouldFindEntityById() {
        // Given
        <Entity> saved = entityManager.persistAndFlush(testEntity);

        // When
        Optional<<Entity>> found = <repository>.findById(saved.getId());

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(saved.getId());
    }

    private <Entity> createTestEntity() {
        return <Entity>.builder()
            .<field>(<value>)
            .build();
    }
}
```

### Integration Test Template

```java
package com.bank.loanapproval.<service>.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
class <Service>IntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
        DockerImageName.parse("postgres:15-alpine")
    );

    @Autowired
    private <Service> <service>;

    @Test
    void should<TestMethod>() {
        // Test implementation
    }
}
```

## Test Coverage Goals

- **Minimum coverage**: 70%
- **Target coverage**: 80%
- **Critical services**: 90%+

## Testing Best Practices

1. **Arrange-Act-Assert (AAA) pattern**: Structure tests clearly
2. **Descriptive test names**: Use `should<ExpectedBehavior>When<StateUnderTest>`
3. **One assertion per test**: Keep tests focused
4. **Mock external dependencies**: Use Mockito for services, repositories
5. **Test edge cases**: Null values, empty collections, boundary conditions
6. **Test exception scenarios**: Verify proper exception handling
7. **Use Testcontainers for integration tests**: Test with real database
8. **Avoid test interdependence**: Each test should be independent

## Common Test Scenarios

### Service Tests
- Create entity successfully
- Update entity successfully
- Delete entity successfully
- Find entity by ID
- Find all entities with pagination
- Throw ResourceNotFoundException when entity not found
- Throw BusinessException for business rule violations
- Throw ValidationException for invalid input

### Controller Tests
- Return correct HTTP status codes
- Return correct response body
- Handle validation errors
- Handle exception scenarios
- Accept valid request payloads
- Reject invalid request payloads

### Repository Tests
- Save entity
- Update entity
- Delete entity
- Find by ID
- Find by custom query
- Count records
- Check existence

## Test Configuration

Create `application-test.yml` in each service:

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:testdb
    driver-class-name: org.h2.Driver
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: true
  kafka:
    bootstrap-servers: localhost:9092
    consumer:
      auto-offset-reset: earliest
```

## Running Tests

```bash
# Run all tests
mvn test

# Run tests for specific module
mvn test -pl <service>

# Run with coverage
mvn test jacoco:report

# Run integration tests
mvn verify -Pintegration-test
```

## Resources

- JUnit 5 Documentation: https://junit.org/junit5/docs/current/user-guide/
- Mockito Documentation: https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html
- Testcontainers Documentation: https://www.testcontainers.org/
- Spring Boot Testing: https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.testing



