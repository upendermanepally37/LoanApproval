package com.bank.loanapproval.eligibilityengine.dto;

import com.bank.loanapproval.common.enumeration.EligibilityStatus;
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
public class EligibilityEvaluationResponse {

    private EligibilityStatus eligibilityStatus;
    private String summary;
    private BigDecimal maxEligibleAmount;
    private Integer maxEligibleTenure;
    private BigDecimal recommendedInterestRate;
    private BigDecimal estimatedEmi;
    private List<RuleEvaluationResult> ruleResults;
    private Integer totalRules;
    private Integer passedRules;
    private Integer failedRules;
    private Boolean hasFraudFlags;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RuleEvaluationResult {
        private String ruleName;
        private String ruleType;
        private String description;
        private Boolean passed;
        private String message;
        private Object actualValue;
        private Object requiredValue;
        private Boolean mandatory;
    }
}
