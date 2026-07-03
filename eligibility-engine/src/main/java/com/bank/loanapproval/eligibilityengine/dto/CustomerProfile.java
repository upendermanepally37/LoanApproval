package com.bank.loanapproval.eligibilityengine.dto;

import com.bank.loanapproval.common.enumeration.KYCStatus;
import com.bank.loanapproval.common.enumeration.RiskProfile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerProfile {

    private Long customerId;
    private String customerNumber;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private Integer age;
    private String gender;
    private String maritalStatus;
    private String panNumber;
    private String aadhaarNumber;
    private KYCStatus kycStatus;
    private RiskProfile riskProfile;
    private String creditScore;
    private Boolean active;

    private List<AddressInfo> addresses;
    private List<EmploymentInfo> employmentDetails;
    private List<ExistingLoanInfo> existingLoans;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddressInfo {
        private String addressType;
        private String city;
        private String state;
        private String postalCode;
        private Boolean isVerified;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmploymentInfo {
        private String employmentType;
        private String employerName;
        private BigDecimal monthlyIncome;
        private BigDecimal annualIncome;
        private LocalDate employmentStartDate;
        private Boolean currentEmployment;
        private String industry;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExistingLoanInfo {
        private String loanType;
        private BigDecimal outstandingAmount;
        private BigDecimal monthlyEmi;
        private String status;
    }
}
