package com.bank.loanapproval.customerservice.mapper;

import com.bank.loanapproval.customerservice.dto.*;
import com.bank.loanapproval.domain.customer.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customerNumber", ignore = true)
    @Mapping(target = "registrationDate", ignore = true)
    @Mapping(target = "kycStatus", ignore = true)
    @Mapping(target = "riskProfile", ignore = true)
    @Mapping(target = "creditScore", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "addresses", ignore = true)
    @Mapping(target = "employmentDetails", ignore = true)
    @Mapping(target = "documents", ignore = true)
    Customer toEntity(CustomerRequest request);

    @Mapping(target = "fullName", expression = "java(entity.getFullName())")
    @Mapping(target = "age", expression = "java(entity.getAge())")
    @Mapping(source = "addresses", target = "addresses", qualifiedByName = "addressToResponse")
    @Mapping(source = "employmentDetails", target = "employmentDetails", qualifiedByName = "employmentToResponse")
    CustomerResponse toResponse(Customer entity);

    @Named("addressToResponse")
    CustomerResponse.AddressResponse toAddressResponse(Address address);

    @Named("employmentToResponse")
    CustomerResponse.EmploymentResponse toEmploymentResponse(EmploymentDetails employment);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "isVerified", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Address toAddressEntity(CustomerRequest.AddressRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "annualIncome", ignore = true)
    @Mapping(target = "currentEmployment", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    EmploymentDetails toEmploymentEntity(CustomerRequest.EmploymentRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "verificationRemarks", ignore = true)
    @Mapping(target = "verifiedAt", ignore = true)
    @Mapping(target = "verifiedBy", ignore = true)
    @Mapping(target = "uploadedAt", ignore = true)
    @Mapping(target = "uploadedBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Document toDocumentEntity(DocumentRequest request);

    DocumentResponse toDocumentResponse(Document document);

    List<CustomerResponse> toResponseList(List<Customer> entities);

    void updateEntityFromRequest(CustomerRequest request, @MappingTarget Customer entity);
}
