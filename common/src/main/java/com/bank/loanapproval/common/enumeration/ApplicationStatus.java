package com.bank.loanapproval.common.enumeration;

import lombok.Getter;

@Getter
public enum ApplicationStatus {
    DRAFT("Draft"),
    SUBMITTED("Submitted"),
    UNDER_REVIEW("Under Review"),
    DOCUMENTS_REQUIRED("Documents Required"),
    ELIGIBILITY_CHECK("Eligibility Check"),
    APPROVED("Approved"),
    REJECTED("Rejected"),
    DISBURSED("Disbursed"),
    WITHDRAWN("Withdrawn"),
    ON_HOLD("On Hold");

    private final String displayName;

    ApplicationStatus(String displayName) {
        this.displayName = displayName;
    }

    public boolean isFinalStatus() {
        return this == APPROVED || this == REJECTED || this == WITHDRAWN || this == DISBURSED;
    }

    public boolean canTransitionTo(ApplicationStatus newStatus) {
        return switch (this) {
            case DRAFT -> newStatus == SUBMITTED || newStatus == WITHDRAWN;
            case SUBMITTED -> newStatus == UNDER_REVIEW || newStatus == DOCUMENTS_REQUIRED || newStatus == WITHDRAWN;
            case UNDER_REVIEW -> newStatus == ELIGIBILITY_CHECK || newStatus == DOCUMENTS_REQUIRED || newStatus == ON_HOLD;
            case DOCUMENTS_REQUIRED -> newStatus == UNDER_REVIEW || newStatus == WITHDRAWN;
            case ELIGIBILITY_CHECK -> newStatus == APPROVED || newStatus == REJECTED || newStatus == ON_HOLD;
            case ON_HOLD -> newStatus == UNDER_REVIEW || newStatus == WITHDRAWN;
            case APPROVED -> newStatus == DISBURSED;
            case REJECTED, WITHDRAWN -> false;
            case DISBURSED -> false;
        };
    }
}
