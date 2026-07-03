package com.bank.loanapproval.loanapplicationservice.mapper;

import com.bank.loanapproval.loanapplicationservice.dto.*;
import com.bank.loanapproval.domain.loanapplication.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LoanApplicationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "applicationNumber", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "loanProduct", ignore = true)
    @Mapping(target = "approvedAmount", ignore = true)
    @Mapping(target = "approvedTenureMonths", ignore = true)
    @Mapping(target = "interestRate", ignore = true)
    @Mapping(target = "processingFee", ignore = true)
    @Mapping(target = "emi", ignore = true)
    @Mapping(target = "repaymentStartDate", ignore = true)
    @Mapping(target = "repaymentEndDate", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "eligibilityStatus", ignore = true)
    @Mapping(target = "rejectionReason", ignore = true)
    @Mapping(target = "fraudFlag", ignore = true)
    @Mapping(target = "fraudRemarks", ignore = true)
    @Mapping(target = "submittedAt", ignore = true)
    @Mapping(target = "approvedAt", ignore = true)
    @Mapping(target = "rejectedAt", ignore = true)
    @Mapping(target = "disbursedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "documents", ignore = true)
    @Mapping(target = "eligibilityChecks", ignore = true)
    @Mapping(target = "statusHistory", ignore = true)
    LoanApplication toEntity(LoanApplicationRequest request);

    @Mapping(target = "customerName", expression = "java(entity.getCustomer() != null ? entity.getCustomer().getFullName() : null)")
    @Mapping(target = "loanProductName", expression = "java(entity.getLoanProduct() != null ? entity.getLoanProduct().getProductName() : null)")
    @Mapping(target = "editable", expression = "java(entity.isEditable())")
    @Mapping(target = "finalStatus", expression = "java(entity.isFinalStatus())")
    @Mapping(source = "documents", target = "documents", qualifiedByName = "documentToResponse")
    @Mapping(source = "eligibilityChecks", target = "eligibilityChecks", qualifiedByName = "eligibilityCheckToResponse")
    LoanApplicationResponse toResponse(LoanApplication entity);

    @Named("documentToResponse")
    ApplicationDocumentResponse toDocumentResponse(ApplicationDocument document);

    @Named("eligibilityCheckToResponse")
    EligibilityCheckResponse toEligibilityCheckResponse(EligibilityCheck check);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "loanApplication", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "verificationRemarks", ignore = true)
    @Mapping(target = "verifiedAt", ignore = true)
    @Mapping(target = "verifiedBy", ignore = true)
    @Mapping(target = "uploadedAt", ignore = true)
    @Mapping(target = "uploadedBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ApplicationDocument toDocumentEntity(ApplicationDocumentRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "loanApplication", ignore = true)
    @Mapping(target = "checkedAt", ignore = true)
    @Mapping(target = "checkedBy", ignore = true)
    EligibilityCheck toEligibilityCheckEntity(EligibilityCheckRequest request);

    List<LoanApplicationResponse> toResponseList(List<LoanApplication> entities);

    void updateEntityFromRequest(LoanApplicationRequest request, @MappingTarget LoanApplication entity);
}
