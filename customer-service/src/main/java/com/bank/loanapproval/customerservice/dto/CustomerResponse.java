package com.bank.loanapproval.customerservice.dto;

import com.bank.loanapproval.common.enumeration.KYCStatus;
import com.bank.loanapproval.common.enumeration.RiskProfile;
import com.bank.loanapproval.domain.customer.Address;
import com.bank.loanapproval.domain.customer.EmploymentDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponse {
    private Long id;
    private String customerNumber;
    private String firstName;
    private String lastName;
    private String middleName;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String alternatePhoneNumber;
    private LocalDate dateOfBirth;
    private Integer age;
    private String gender;
    private String maritalStatus;
    private String nationality;
    private String panNumber;
    private String aadhaarNumber;
    private LocalDateTime registrationDate;
    private KYCStatus kycStatus;
    private String kycRemarks;
    private RiskProfile riskProfile;
    private String creditScore;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<AddressResponse> addresses;
    private List<EmploymentResponse> employmentDetails;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddressResponse {
        private Long id;
        private Address.AddressType addressType;
        private String addressLine1;
        private String addressLine2;
        private String city;
        private String state;
        private String postalCode;
        private String country;
        private Boolean isVerified;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmploymentResponse {
        private Long id;
        private EmploymentDetails.EmploymentType employmentType;
        private String employerName;
        private String employerAddress;
        private String designation;
        private String department;
        private java.math.BigDecimal monthlyIncome;
        private java.math.BigDecimal annualIncome;
        private String employmentId;
        private LocalDate employmentStartDate;
        private LocalDate employmentEndDate;
        private String workLocation;
        private String industry;
        private Boolean currentEmployment;
    }
}
