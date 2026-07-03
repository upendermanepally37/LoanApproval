package com.bank.loanapproval.eligibilityengine.rule.impl;

import com.bank.loanapproval.eligibilityengine.dto.CustomerProfile;
import com.bank.loanapproval.eligibilityengine.dto.EligibilityEvaluationResponse;
import com.bank.loanapproval.eligibilityengine.dto.LoanProductInfo;
import com.bank.loanapproval.eligibilityengine.rule.EligibilityRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CreditScoreRule implements EligibilityRule {

    @Override
    public String getRuleName() {
        return "CREDIT_SCORE_CHECK";
    }

    @Override
    public String getRuleType() {
        return "CREDIT_SCORE";
    }

    @Override
    public String getDescription() {
        return "Customer's credit score must meet the minimum requirement";
    }

    @Override
    public Boolean isMandatory() {
        return true;
    }

    @Override
    public EligibilityEvaluationResponse.RuleEvaluationResult evaluate(
            CustomerProfile customerProfile,
            LoanProductInfo loanProduct,
            java.math.BigDecimal requestedAmount,
            Integer requestedTenureMonths) {

        Integer creditScore = 0;
        try {
            if (customerProfile.getCreditScore() != null && !customerProfile.getCreditScore().isEmpty()) {
                creditScore = Integer.parseInt(customerProfile.getCreditScore());
            }
        } catch (NumberFormatException e) {
            log.warn("Invalid credit score format for customer: {}", customerProfile.getCustomerId());
        }

        Integer minCreditScore = loanProduct.getMinCreditScore();

        boolean passed = creditScore >= minCreditScore;

        return EligibilityEvaluationResponse.RuleEvaluationResult.builder()
                .ruleName(getRuleName())
                .ruleType(getRuleType())
                .description(getDescription())
                .passed(passed)
                .message(passed ? "Credit score meets minimum requirement" :
                        String.format("Credit score %d is below minimum requirement %d", creditScore, minCreditScore))
                .actualValue(creditScore)
                .requiredValue(minCreditScore)
                .mandatory(isMandatory())
                .build();
    }
}
