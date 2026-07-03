package com.bank.loanapproval.eligibilityengine.rule.impl;

import com.bank.loanapproval.eligibilityengine.dto.CustomerProfile;
import com.bank.loanapproval.eligibilityengine.dto.EligibilityEvaluationResponse;
import com.bank.loanapproval.eligibilityengine.dto.LoanProductInfo;
import com.bank.loanapproval.eligibilityengine.rule.EligibilityRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
@Component
public class DebtToIncomeRatioRule implements EligibilityRule {

    @Override
    public String getRuleName() {
        return "DEBT_TO_INCOME_RATIO_CHECK";
    }

    @Override
    public String getRuleType() {
        return "DEBT_TO_INCOME_RATIO";
    }

    @Override
    public String getDescription() {
        return "Customer's debt-to-income ratio must be within acceptable limits";
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
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal existingMonthlyEmi = customerProfile.getExistingLoans().stream()
                .map(loan -> loan.getMonthlyEmi() != null ? loan.getMonthlyEmi() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal newLoanEmi = calculateEmi(requestedAmount, loanProduct.getInterestRate(), requestedTenureMonths);

        BigDecimal totalMonthlyDebt = existingMonthlyEmi.add(newLoanEmi);

        BigDecimal debtToIncomeRatio = monthlyIncome.compareTo(BigDecimal.ZERO) > 0 ?
                totalMonthlyDebt.divide(monthlyIncome, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)) :
                BigDecimal.valueOf(100);

        BigDecimal maxRatio = loanProduct.getDebtToIncomeRatio();

        boolean passed = debtToIncomeRatio.compareTo(maxRatio) <= 0;

        return EligibilityEvaluationResponse.RuleEvaluationResult.builder()
                .ruleName(getRuleName())
                .ruleType(getRuleType())
                .description(getDescription())
                .passed(passed)
                .message(passed ? "Debt-to-income ratio is within acceptable limits" :
                        String.format("Debt-to-income ratio %.2f%% exceeds maximum %.2f%%", 
                                debtToIncomeRatio, maxRatio))
                .actualValue(debtToIncomeRatio)
                .requiredValue(maxRatio)
                .mandatory(isMandatory())
                .build();
    }

    private BigDecimal calculateEmi(BigDecimal principal, BigDecimal annualRate, Integer months) {
        BigDecimal monthlyRate = annualRate.divide(BigDecimal.valueOf(1200), 10, RoundingMode.HALF_UP);
        return principal.multiply(monthlyRate)
                .multiply(BigDecimal.ONE.add(monthlyRate).pow(months))
                .divide(BigDecimal.ONE.add(monthlyRate).pow(months).subtract(BigDecimal.ONE), 
                        2, RoundingMode.HALF_UP);
    }
}
