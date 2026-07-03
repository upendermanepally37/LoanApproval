package com.bank.loanapproval.loanproductservice.dto;

import com.bank.loanapproval.common.enumeration.LoanType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanProductResponse {
    private Long id;
    private String productCode;
    private String productName;
    private LoanType loanType;
    private String description;
    private BigDecimal interestRate;
    private BigDecimal processingFee;
    private BigDecimal processingFeePercentage;
    private Integer minTenureMonths;
    private Integer maxTenureMonths;
    private BigDecimal minLoanAmount;
    private BigDecimal maxLoanAmount;
    private BigDecimal minMonthlySalary;
    private Integer minCreditScore;
    private Integer minAge;
    private Integer maxAge;
    private Boolean prepaymentAllowed;
    private BigDecimal prepaymentFee;
    private Boolean partPaymentAllowed;
    private BigDecimal debtToIncomeRatio;
    private String repaymentFrequency;
    private String eligibilityCriteria;
    private String requiredDocuments;
    private Boolean active;
    private LocalDateTime effectiveFrom;
    private LocalDateTime effectiveTo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<EligibilityRuleResponse> eligibilityRules;
}
