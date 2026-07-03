package com.bank.loanapproval.eligibilityengine.rule.impl;

import com.bank.loanapproval.eligibilityengine.dto.CustomerProfile;
import com.bank.loanapproval.eligibilityengine.dto.EligibilityEvaluationResponse;
import com.bank.loanapproval.eligibilityengine.dto.LoanProductInfo;
import com.bank.loanapproval.eligibilityengine.rule.EligibilityRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AgeRule implements EligibilityRule {

    @Override
    public String getRuleName() {
        return "AGE_CHECK";
    }

    @Override
    public String getRuleType() {
        return "AGE";
    }

    @Override
    public String getDescription() {
        return "Customer age must be within the product's age range";
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

        Integer age = customerProfile.getAge();
        Integer minAge = loanProduct.getMinAge();
        Integer maxAge = loanProduct.getMaxAge();

        boolean passed = age >= minAge && age <= maxAge;

        return EligibilityEvaluationResponse.RuleEvaluationResult.builder()
                .ruleName(getRuleName())
                .ruleType(getRuleType())
                .description(getDescription())
                .passed(passed)
                .message(passed ? "Age is within acceptable range" : 
                        String.format("Age %d is outside acceptable range %d-%d", age, minAge, maxAge))
                .actualValue(age)
                .requiredValue(String.format("%d-%d", minAge, maxAge))
                .mandatory(isMandatory())
                .build();
    }
}
