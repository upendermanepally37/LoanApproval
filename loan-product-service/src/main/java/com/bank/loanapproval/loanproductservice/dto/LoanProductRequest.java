package com.bank.loanapproval.loanproductservice.dto;

import com.bank.loanapproval.common.enumeration.LoanType;
import com.bank.loanapproval.domain.loanproduct.EligibilityRule;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
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
public class LoanProductRequest {

    @NotBlank(message = "Product code is required")
    @Size(max = 50, message = "Product code must not exceed 50 characters")
    private String productCode;

    @NotBlank(message = "Product name is required")
    @Size(max = 100, message = "Product name must not exceed 100 characters")
    private String productName;

    @NotNull(message = "Loan type is required")
    private LoanType loanType;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @NotNull(message = "Interest rate is required")
    @DecimalMin(value = "0.01", message = "Interest rate must be greater than 0")
    @DecimalMax(value = "100", message = "Interest rate must not exceed 100")
    private BigDecimal interestRate;

    @NotNull(message = "Processing fee is required")
    @DecimalMin(value = "0", message = "Processing fee must be non-negative")
    private BigDecimal processingFee;

    @NotNull(message = "Processing fee percentage is required")
    @DecimalMin(value = "0", message = "Processing fee percentage must be non-negative")
    @DecimalMax(value = "100", message = "Processing fee percentage must not exceed 100")
    private BigDecimal processingFeePercentage;

    @NotNull(message = "Minimum tenure is required")
    @Min(value = 1, message = "Minimum tenure must be at least 1 month")
    private Integer minTenureMonths;

    @NotNull(message = "Maximum tenure is required")
    @Min(value = 1, message = "Maximum tenure must be at least 1 month")
    private Integer maxTenureMonths;

    @NotNull(message = "Minimum loan amount is required")
    @DecimalMin(value = "1000", message = "Minimum loan amount must be at least 1000")
    private BigDecimal minLoanAmount;

    @NotNull(message = "Maximum loan amount is required")
    @DecimalMin(value = "1000", message = "Maximum loan amount must be at least 1000")
    private BigDecimal maxLoanAmount;

    @NotNull(message = "Minimum monthly salary is required")
    @DecimalMin(value = "0", message = "Minimum monthly salary must be non-negative")
    private BigDecimal minMonthlySalary;

    @NotNull(message = "Minimum credit score is required")
    @Min(value = 300, message = "Minimum credit score must be at least 300")
    @Max(value = 850, message = "Maximum credit score must not exceed 850")
    private Integer minCreditScore;

    @NotNull(message = "Minimum age is required")
    @Min(value = 18, message = "Minimum age must be at least 18")
    private Integer minAge;

    @NotNull(message = "Maximum age is required")
    @Max(value = 100, message = "Maximum age must not exceed 100")
    private Integer maxAge;

    private Boolean prepaymentAllowed;

    @DecimalMin(value = "0", message = "Prepayment fee must be non-negative")
    private BigDecimal prepaymentFee;

    private Boolean partPaymentAllowed;

    @NotNull(message = "Debt to income ratio is required")
    @DecimalMin(value = "0", message = "Debt to income ratio must be non-negative")
    @DecimalMax(value = "100", message = "Debt to income ratio must not exceed 100")
    private BigDecimal debtToIncomeRatio;

    @Size(max = 20, message = "Repayment frequency must not exceed 20 characters")
    private String repaymentFrequency;

    @Size(max = 1000, message = "Eligibility criteria must not exceed 1000 characters")
    private String eligibilityCriteria;

    @Size(max = 1000, message = "Required documents must not exceed 1000 characters")
    private String requiredDocuments;

    private LocalDateTime effectiveFrom;

    private LocalDateTime effectiveTo;

    @Valid
    private List<EligibilityRuleRequest> eligibilityRules;
}
