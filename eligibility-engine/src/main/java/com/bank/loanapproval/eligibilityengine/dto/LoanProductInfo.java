package com.bank.loanapproval.eligibilityengine.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanProductInfo {

    private Long productId;
    private String productCode;
    private String productName;
    private String loanType;
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
    private BigDecimal debtToIncomeRatio;
    private List<EligibilityRuleInfo> eligibilityRules;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EligibilityRuleInfo {
        private String ruleName;
        private String ruleType;
        private String ruleExpression;
        private BigDecimal minValue;
        private BigDecimal maxValue;
        private String stringValue;
        private Integer priority;
        private Boolean mandatory;
    }
}
