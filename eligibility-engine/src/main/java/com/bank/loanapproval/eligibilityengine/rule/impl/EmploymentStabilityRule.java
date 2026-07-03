package com.bank.loanapproval.eligibilityengine.rule.impl;

import com.bank.loanapproval.eligibilityengine.dto.CustomerProfile;
import com.bank.loanapproval.eligibilityengine.dto.EligibilityEvaluationResponse;
import com.bank.loanapproval.eligibilityengine.dto.LoanProductInfo;
import com.bank.loanapproval.eligibilityengine.rule.EligibilityRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;

@Slf4j
@Component
public class EmploymentStabilityRule implements EligibilityRule {

    private static final int MIN_EMPLOYMENT_YEARS = 1;

    @Override
    public String getRuleName() {
        return "EMPLOYMENT_STABILITY_CHECK";
    }

    @Override
    public String getRuleType() {
        return "EMPLOYMENT_STABILITY";
    }

    @Override
    public String getDescription() {
        return "Customer must have stable employment for at least " + MIN_EMPLOYMENT_YEARS + " year(s)";
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

        LocalDate currentEmploymentStartDate = customerProfile.getEmploymentDetails().stream()
                .filter(CustomerProfile.EmploymentInfo::getCurrentEmployment)
                .map(CustomerProfile.EmploymentInfo::getEmploymentStartDate)
                .filter(date -> date != null)
                .findFirst()
                .orElse(null);

        boolean passed = false;
        String message = "No current employment information available";
        Object actualValue = "N/A";

        if (currentEmploymentStartDate != null) {
            Period period = Period.between(currentEmploymentStartDate, LocalDate.now());
            int years = period.getYears();
            passed = years >= MIN_EMPLOYMENT_YEARS;
            actualValue = years + " years";
            message = passed ? "Employment stability meets requirement" :
                    String.format("Current employment duration %d years is below minimum %d years", 
                            years, MIN_EMPLOYMENT_YEARS);
        }

        return EligibilityEvaluationResponse.RuleEvaluationResult.builder()
                .ruleName(getRuleName())
                .ruleType(getRuleType())
                .description(getDescription())
                .passed(passed)
                .message(message)
                .actualValue(actualValue)
                .requiredValue(MIN_EMPLOYMENT_YEARS + "+ years")
                .mandatory(isMandatory())
                .build();
    }
}
