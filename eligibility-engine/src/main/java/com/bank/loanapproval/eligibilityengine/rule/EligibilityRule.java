package com.bank.loanapproval.eligibilityengine.rule;

import com.bank.loanapproval.eligibilityengine.dto.CustomerProfile;
import com.bank.loanapproval.eligibilityengine.dto.EligibilityEvaluationResponse;
import com.bank.loanapproval.eligibilityengine.dto.LoanProductInfo;

public interface EligibilityRule {

    String getRuleName();

    String getRuleType();

    String getDescription();

    Boolean isMandatory();

    EligibilityEvaluationResponse.RuleEvaluationResult evaluate(
            CustomerProfile customerProfile,
            LoanProductInfo loanProduct,
            java.math.BigDecimal requestedAmount,
            Integer requestedTenureMonths
    );
}
