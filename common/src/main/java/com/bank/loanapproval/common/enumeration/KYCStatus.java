package com.bank.loanapproval.common.enumeration;

import lombok.Getter;

@Getter
public enum KYCStatus {
    NOT_STARTED("Not Started"),
    IN_PROGRESS("In Progress"),
    PENDING_VERIFICATION("Pending Verification"),
    VERIFIED("Verified"),
    REJECTED("Rejected");

    private final String displayName;

    KYCStatus(String displayName) {
        this.displayName = displayName;
    }
}
