package com.bank.loanapproval.common.util;

import org.springframework.stereotype.Component;

@Component
public class NumberGenerator {

    private static final String CUSTOMER_PREFIX = "CUST";
    private static final String LOAN_APPLICATION_PREFIX = "LA";
    private static final int CUSTOMER_SUFFIX_LENGTH = 7;
    private static final int LOAN_APPLICATION_SUFFIX_LENGTH = 10;

    public String generateCustomerNumber() {
        long timestamp = System.currentTimeMillis();
        return CUSTOMER_PREFIX + extractSuffix(timestamp, CUSTOMER_SUFFIX_LENGTH);
    }

    public String generateLoanApplicationNumber() {
        long timestamp = System.currentTimeMillis();
        return LOAN_APPLICATION_PREFIX + extractSuffix(timestamp, LOAN_APPLICATION_SUFFIX_LENGTH);
    }

    private String extractSuffix(long timestamp, int length) {
        String timestampStr = String.valueOf(timestamp);
        return timestampStr.substring(Math.max(0, timestampStr.length() - length));
    }
}
