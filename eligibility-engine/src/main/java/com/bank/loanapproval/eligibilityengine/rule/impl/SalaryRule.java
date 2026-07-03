package com.bank.loanapproval.eligibilityengine.rule.impl;

import com.bank.loanapproval.eligibilityengine.dto.CustomerProfile;
import com.bank.loanapproval.eligibilityengine.dto.EligibilityEvaluationResponse;
import com.bank.loanapproval.eligibilityengine.dto.LoanProductInfo;
import com.bank.loanapproval.eligibilityengine.rule.EligibilityRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Comparator;

@Slf4j
@Component
public class SalaryRule implements EligibilityRule {

    @Override
    public String getRuleName() {
        return "SALARY_CHECK";
    }

    @Override
    public String getRuleType() {
        return "SALARY";
    }

    @Override
    public String getDescription() {
        return "Customer's monthly income must meet the minimum salary requirement";
    }

    @Override
    public Boolean isMandatory() {
        return true;
    }

    @Override
    public EligibilityEvaluationResponse.RuleEvaluationResult evaluate(
            CustomerProfile customerProfile,
            LoanProductInfo loanProduct,
            BigDecimal requestedAmount,
            Integer requestedTenureMonths) {

        BigDecimal monthlyIncome = customerProfile.getEmploymentDetails().stream()
                .filter(CustomerProfile.EmploymentInfo::getCurrentEmployment)
                .map(CustomerProfile.EmploymentInfo::getMonthlyIncome)
                .max(Comparator.naturalOrder())
                .orElse(BigDecimal.ZERO);

        BigDecimal minSalary = loanProduct.getMinMonthlySalary();

        boolean passed = monthlyIncome.compareTo(minSalary) >= 0;

        return EligibilityEvaluationResponse.RuleEvaluationResult.builder()
                .ruleName(getRuleName())
                .ruleType(getRuleType())
                .description(getDescription())
                .passed(passed)
                .message(passed ? "Monthly income meets minimum requirement" :
                        String.format("Monthly income %s is below minimum requirement %s", monthlyIncome, minSalary))
                .actualValue(monthlyIncome)
                .requiredValue(minSalary)
                .mandatory(isMandatory())
                .build();
    }
}
