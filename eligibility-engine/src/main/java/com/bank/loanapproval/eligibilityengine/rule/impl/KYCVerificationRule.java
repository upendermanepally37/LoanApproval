package com.bank.loanapproval.eligibilityengine.rule.impl;

import com.bank.loanapproval.common.enumeration.KYCStatus;
import com.bank.loanapproval.eligibilityengine.dto.CustomerProfile;
import com.bank.loanapproval.eligibilityengine.dto.EligibilityEvaluationResponse;
import com.bank.loanapproval.eligibilityengine.dto.LoanProductInfo;
import com.bank.loanapproval.eligibilityengine.rule.EligibilityRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KYCVerificationRule implements EligibilityRule {

    @Override
    public String getRuleName() {
        return "KYC_VERIFICATION_CHECK";
    }

    @Override
    public String getRuleType() {
        return "KYC";
    }

    @Override
    public String getDescription() {
        return "Customer must have completed KYC verification";
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

        KYCStatus kycStatus = customerProfile.getKycStatus();
        boolean passed = kycStatus == KYCStatus.VERIFIED;

        return EligibilityEvaluationResponse.RuleEvaluationResult.builder()
                .ruleName(getRuleName())
                .ruleType(getRuleType())
                .description(getDescription())
                .passed(passed)
                .message(passed ? "KYC verification completed" :
                        String.format("KYC verification status is %s", kycStatus.getDisplayName()))
                .actualValue(kycStatus.getDisplayName())
                .requiredValue("VERIFIED")
                .mandatory(isMandatory())
                .build();
    }
}
