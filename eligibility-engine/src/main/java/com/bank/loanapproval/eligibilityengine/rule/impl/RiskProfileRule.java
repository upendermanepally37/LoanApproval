package com.bank.loanapproval.eligibilityengine.rule.impl;

import com.bank.loanapproval.common.enumeration.RiskProfile;
import com.bank.loanapproval.eligibilityengine.dto.CustomerProfile;
import com.bank.loanapproval.eligibilityengine.dto.EligibilityEvaluationResponse;
import com.bank.loanapproval.eligibilityengine.dto.LoanProductInfo;
import com.bank.loanapproval.eligibilityengine.rule.EligibilityRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RiskProfileRule implements EligibilityRule {

    @Override
    public String getRuleName() {
        return "RISK_PROFILE_CHECK";
    }

    @Override
    public String getRuleType() {
        return "RISK_PROFILE";
    }

    @Override
    public String getDescription() {
        return "Customer's risk profile must be acceptable (LOW or MEDIUM)";
    }

    @Override
    public Boolean isMandatory() {
        return false;
    }

    @Override
    public EligibilityEvaluationResponse.RuleEvaluationResult evaluate(
            CustomerProfile customerProfile,
            LoanProductInfo loanProduct,
            java.math.BigDecimal requestedAmount,
            Integer requestedTenureMonths) {

        RiskProfile riskProfile = customerProfile.getRiskProfile();
        boolean passed = riskProfile == RiskProfile.LOW || riskProfile == RiskProfile.MEDIUM;

        return EligibilityEvaluationResponse.RuleEvaluationResult.builder()
                .ruleName(getRuleName())
                .ruleType(getRuleType())
                .description(getDescription())
                .passed(passed)
                .message(passed ? "Risk profile is acceptable" :
                        String.format("Risk profile %s may require additional review", riskProfile.getDisplayName()))
                .actualValue(riskProfile != null ? riskProfile.getDisplayName() : "NOT ASSESSED")
                .requiredValue("LOW or MEDIUM")
                .mandatory(isMandatory())
                .build();
    }
}
