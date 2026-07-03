---
description: Code Refactoring using Multiple Subagents
---

## Overview

This workflow provides a systematic approach to refactoring application code using multiple parallel analysis techniques ("subagents") to identify improvement opportunities and implement changes efficiently.

## Refactoring Strategy

### 1. Parallel Code Analysis

Use multiple parallel searches to identify refactoring opportunities across different aspects:

**Code Quality Issues:**
- Search for `TODO`, `FIXME`, `XXX` comments
- Search for `System.out.print` or `System.err.print` statements
- Search for deprecated annotations
- Search for code duplication patterns

**Architecture & Design:**
- Identify service classes with `@Service` annotation
- Identify controller classes with `@RestController` or `@Controller`
- Identify repository classes with `@Repository` or extending JpaRepository
- Identify mapper classes with `@Mapper` annotation

**Technical Debt:**
- Search for long methods (>50 lines)
- Search for classes with high complexity
- Search for duplicated logic patterns
- Search for circular dependencies

### 2. Common Refactoring Patterns

**Extract Utility Classes:**
- Identify duplicated logic across multiple services
- Create utility classes in common module
- Example: NumberGenerator, EventPublisher, RestTemplateConfig

**Improve Domain Models:**
- Add `@EqualsAndHashCode.Exclude` to bidirectional relationships
- Add `@ToString.Exclude` to prevent circular toString issues
- Ensure proper use of Lombok annotations

**Service Layer Improvements:**
- Extract validation logic to separate classes
- Use dependency injection instead of creating instances
- Remove hardcoded values and move to configuration

**Mapper Simplification:**
- Review MapStruct mappers for excessive @Mapping annotations
- Use @MappingTarget for update operations
- Implement @Named methods for complex transformations

### 3. Parallel Implementation

When implementing refactoring changes:

**Phase 1 - Infrastructure:**
- Create utility classes and configurations
- Add necessary dependencies to pom.xml files
- Update domain models with proper annotations

**Phase 2 - Service Updates:**
- Update services to use new utilities
- Remove duplicate methods
- Simplify service logic

**Phase 3 - Testing:**
- Build project with `mvn clean install -DskipTests`
- Run affected unit tests
- Verify no compilation errors

### 4. Refactoring Checklist

**Before Refactoring:**
- [ ] Identify all files that need changes
- [ ] Ensure tests exist for affected code
- [ ] Create feature branch if working on large changes

**During Refactoring:**
- [ ] Make small, incremental changes
- [ ] Build after each significant change
- [ ] Run tests after each change
- [ ] Keep changes focused on single improvement

**After Refactoring:**
- [ ] All tests pass
- [ ] No compilation warnings
- [ ] Code is more readable/maintainable
- [ ] No functionality is broken
- [ ] Update documentation if needed

### 5. Best Practices

**DRY Principle:**
- Extract duplicated code to reusable utilities
- Use inheritance and composition appropriately
- Leverage Spring's dependency injection

**SOLID Principles:**
- Single Responsibility: Each class has one reason to change
- Open/Closed: Open for extension, closed for modification
- Liskov Substitution: Subtypes must be substitutable
- Interface Segregation: Prefer specific interfaces
- Dependency Inversion: Depend on abstractions

**Performance:**
- Use singleton beans instead of creating instances
- Lazy load where appropriate
- Avoid unnecessary database queries

**Maintainability:**
- Use meaningful names for classes, methods, variables
- Keep methods short and focused
- Add comments for complex logic
- Follow consistent coding standards

## Steps

1. **Parallel Analysis Phase**
   - Run multiple parallel searches to identify refactoring opportunities
   - Analyze service, controller, repository, and mapper layers
   - Identify code duplication, technical debt, and design issues

2. **Planning Phase**
   - Prioritize refactoring opportunities by impact
   - Create refactoring plan with phases
   - Identify dependencies between changes

3. **Implementation Phase**
   - Create utility classes and configurations first
   - Update domain models with proper annotations
   - Refactor services to use new utilities
   - Remove duplicate code and methods

4. **Testing Phase**
   - Build project with `mvn clean install -DskipTests`
   - Run unit tests for affected modules
   - Verify no compilation errors or warnings
   - Test functionality manually if needed

5. **Documentation Phase**
   - Update relevant documentation
   - Add comments for complex refactoring
   - Update workflow files if patterns are reusable

6. **Commit Phase**
   - Stage all refactored files
   - Create descriptive commit message
   - Push changes to remote repository

