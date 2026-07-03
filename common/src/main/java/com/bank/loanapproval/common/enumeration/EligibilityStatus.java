package com.bank.loanapproval.common.enumeration;

import lombok.Getter;

@Getter
public enum EligibilityStatus {
    ELIGIBLE("Eligible"),
    PARTIALLY_ELIGIBLE("Partially Eligible"),
    REJECTED("Rejected");

    private final String displayName;

    EligibilityStatus(String displayName) {
        this.displayName = displayName;
    }
}
