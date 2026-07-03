package com.bank.loanapproval.common.enumeration;

import lombok.Getter;

@Getter
public enum LoanType {
    PERSONAL_LOAN("Personal Loan"),
    HOME_LOAN("Home Loan"),
    VEHICLE_LOAN("Vehicle Loan"),
    BUSINESS_LOAN("Business Loan"),
    EDUCATION_LOAN("Education Loan"),
    GOLD_LOAN("Gold Loan");

    private final String displayName;

    LoanType(String displayName) {
        this.displayName = displayName;
    }
}
