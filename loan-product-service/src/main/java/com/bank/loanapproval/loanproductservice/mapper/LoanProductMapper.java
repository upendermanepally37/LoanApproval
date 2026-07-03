package com.bank.loanapproval.loanproductservice.mapper;

import com.bank.loanapproval.loanproductservice.dto.EligibilityRuleRequest;
import com.bank.loanapproval.loanproductservice.dto.EligibilityRuleResponse;
import com.bank.loanapproval.loanproductservice.dto.LoanProductRequest;
import com.bank.loanapproval.loanproductservice.dto.LoanProductResponse;
import com.bank.loanapproval.domain.loanproduct.EligibilityRule;
import com.bank.loanapproval.domain.loanproduct.LoanProduct;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LoanProductMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "eligibilityRules", ignore = true)
    LoanProduct toEntity(LoanProductRequest request);

    @Mapping(source = "eligibilityRules", target = "eligibilityRules", qualifiedByName = "eligibilityRuleToResponse")
    LoanProductResponse toResponse(LoanProduct entity);

    @Named("eligibilityRuleToResponse")
    EligibilityRuleResponse toEligibilityRuleResponse(EligibilityRule rule);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "loanProduct", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    EligibilityRule toEligibilityRuleEntity(EligibilityRuleRequest request);

    List<LoanProductResponse> toResponseList(List<LoanProduct> entities);

    void updateEntityFromRequest(LoanProductRequest request, @MappingTarget LoanProduct entity);
}
