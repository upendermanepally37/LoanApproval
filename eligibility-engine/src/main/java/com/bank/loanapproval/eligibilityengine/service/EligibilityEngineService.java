package com.bank.loanapproval.eligibilityengine.service;

import com.bank.loanapproval.common.enumeration.EligibilityStatus;
import com.bank.loanapproval.eligibilityengine.dto.*;
import com.bank.loanapproval.eligibilityengine.rule.EligibilityRule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EligibilityEngineService {

    private final List<EligibilityRule> eligibilityRules;
    private final CustomerDataService customerDataService;
    private final LoanProductDataService loanProductDataService;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public EligibilityEvaluationResponse evaluateEligibility(EligibilityEvaluationRequest request) {
        log.info("Evaluating eligibility for customer: {} and loan product: {}", 
                request.getCustomerId(), request.getLoanProductId());

        CustomerProfile customerProfile = customerDataService.getCustomerProfile(request.getCustomerId());
        LoanProductInfo loanProduct = loanProductDataService.getLoanProductInfo(request.getLoanProductId());

        List<EligibilityEvaluationResponse.RuleEvaluationResult> ruleResults = eligibilityRules.stream()
                .sorted(Comparator.comparing(rule -> rule.isMandatory() ? 0 : 1))
                .map(rule -> rule.evaluate(customerProfile, loanProduct, 
                        request.getRequestedAmount(), request.getRequestedTenureMonths()))
                .toList();

        long mandatoryFailed = ruleResults.stream()
                .filter(result -> result.getMandatory() && !result.getPassed())
                .count();
        long totalFailed = ruleResults.stream().filter(result -> !result.getPassed()).count();
        long totalPassed = ruleResults.stream().filter(EligibilityEvaluationResponse.RuleEvaluationResult::getPassed).count();

        EligibilityStatus eligibilityStatus;
        String summary;
        BigDecimal maxEligibleAmount = request.getRequestedAmount();
        Integer maxEligibleTenure = request.getRequestedTenureMonths();

        if (mandatoryFailed > 0) {
            eligibilityStatus = EligibilityStatus.REJECTED;
            summary = "Application rejected due to failed mandatory eligibility checks";
            maxEligibleAmount = BigDecimal.ZERO;
            maxEligibleTenure = 0;
        } else if (totalFailed > 0) {
            eligibilityStatus = EligibilityStatus.PARTIALLY_ELIGIBLE;
            summary = "Application partially eligible - some non-mandatory checks failed";
            maxEligibleAmount = calculateMaxEligibleAmount(customerProfile, loanProduct);
            maxEligibleTenure = loanProduct.getMaxTenureMonths();
        } else {
            eligibilityStatus = EligibilityStatus.ELIGIBLE;
            summary = "Application eligible - all checks passed";
            maxEligibleAmount = calculateMaxEligibleAmount(customerProfile, loanProduct);
            maxEligibleTenure = loanProduct.getMaxTenureMonths();
        }

        BigDecimal estimatedEmi = calculateEmi(request.getRequestedAmount(), loanProduct.getInterestRate(), 
                request.getRequestedTenureMonths());

        boolean hasFraudFlags = checkFraudFlags(customerProfile);

        EligibilityEvaluationResponse response = EligibilityEvaluationResponse.builder()
                .eligibilityStatus(eligibilityStatus)
                .summary(summary)
                .maxEligibleAmount(maxEligibleAmount)
                .maxEligibleTenure(maxEligibleTenure)
                .recommendedInterestRate(loanProduct.getInterestRate())
                .estimatedEmi(estimatedEmi)
                .ruleResults(ruleResults)
                .totalRules(ruleResults.size())
                .passedRules((int) totalPassed)
                .failedRules((int) totalFailed)
                .hasFraudFlags(hasFraudFlags)
                .build();

        log.info("Eligibility evaluation completed for customer: {} with status: {}", 
                request.getCustomerId(), eligibilityStatus);

        publishEligibilityResult(request, response);
        return response;
    }

    private BigDecimal calculateMaxEligibleAmount(CustomerProfile customerProfile, LoanProductInfo loanProduct) {
        BigDecimal monthlyIncome = customerProfile.getEmploymentDetails().stream()
                .filter(CustomerProfile.EmploymentInfo::getCurrentEmployment)
                .map(CustomerProfile.EmploymentInfo::getMonthlyIncome)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal existingEmi = customerProfile.getExistingLoans().stream()
                .map(loan -> loan.getMonthlyEmi() != null ? loan.getMonthlyEmi() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal availableIncome = monthlyIncome.subtract(existingEmi);
        BigDecimal maxDebtRatio = loanProduct.getDebtToIncomeRatio().divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
        BigDecimal maxEmi = monthlyIncome.multiply(maxDebtRatio).subtract(existingEmi);

        if (maxEmi.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        int maxTenure = loanProduct.getMaxTenureMonths();
        BigDecimal monthlyRate = loanProduct.getInterestRate().divide(BigDecimal.valueOf(1200), 10, RoundingMode.HALF_UP);
        
        BigDecimal maxLoanAmount = maxEmi.multiply(
                BigDecimal.ONE.add(monthlyRate).pow(maxTenure).subtract(BigDecimal.ONE))
                .divide(monthlyRate.multiply(BigDecimal.ONE.add(monthlyRate).pow(maxTenure)), 
                        2, RoundingMode.HALF_DOWN);

        return maxLoanAmount.min(loanProduct.getMaxLoanAmount());
    }

    private BigDecimal calculateEmi(BigDecimal principal, BigDecimal annualRate, Integer months) {
        BigDecimal monthlyRate = annualRate.divide(BigDecimal.valueOf(1200), 10, RoundingMode.HALF_UP);
        return principal.multiply(monthlyRate)
                .multiply(BigDecimal.ONE.add(monthlyRate).pow(months))
                .divide(BigDecimal.ONE.add(monthlyRate).pow(months).subtract(BigDecimal.ONE), 
                        2, RoundingMode.HALF_UP);
    }

    private boolean checkFraudFlags(CustomerProfile customerProfile) {
        return customerProfile.getExistingLoans().stream()
                .filter(loan -> "FRAUD".equalsIgnoreCase(loan.getStatus()) || 
                              "DEFAULT".equalsIgnoreCase(loan.getStatus()))
                .count() > 0;
    }

    private void publishEligibilityResult(EligibilityEvaluationRequest request, EligibilityEvaluationResponse response) {
        try {
            kafkaTemplate.send("eligibility-results", request.getCustomerId().toString(), response);
        } catch (Exception e) {
            log.error("Failed to publish eligibility result", e);
        }
    }
}
