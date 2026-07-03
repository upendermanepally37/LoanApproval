package com.bank.loanapproval.common.enumeration;

import lombok.Getter;

@Getter
public enum RiskProfile {
    LOW("Low Risk"),
    MEDIUM("Medium Risk"),
    HIGH("High Risk"),
    VERY_HIGH("Very High Risk");

    private final String displayName;

    RiskProfile(String displayName) {
        this.displayName = displayName;
    }
}
