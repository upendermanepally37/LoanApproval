package com.bank.loanapproval.customerservice.dto;

import com.bank.loanapproval.domain.customer.Address;
import com.bank.loanapproval.domain.customer.EmploymentDetails;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRequest {

    @NotBlank(message = "First name is required")
    @Size(max = 100, message = "First name must not exceed 100 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 100, message = "Last name must not exceed 100 characters")
    private String lastName;

    @Size(max = 100, message = "Middle name must not exceed 100 characters")
    private String middleName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10,20}$", message = "Phone number must be 10-20 digits")
    private String phoneNumber;

    @Pattern(regexp = "^[0-9]{10,20}$", message = "Alternate phone number must be 10-20 digits")
    private String alternatePhoneNumber;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Gender is required")
    @Pattern(regexp = "^(M|F|O)$", message = "Gender must be M, F, or O")
    private String gender;

    @NotBlank(message = "Marital status is required")
    @Pattern(regexp = "^(S|M|D|W)$", message = "Marital status must be S, M, D, or W")
    private String maritalStatus;

    @Size(max = 100, message = "Nationality must not exceed 100 characters")
    private String nationality;

    @Pattern(regexp = "^[A-Z]{5}[0-9]{4}[A-Z]{1}$", message = "Invalid PAN number format")
    private String panNumber;

    @Pattern(regexp = "^[0-9]{12}$", message = "Aadhaar number must be 12 digits")
    private String aadhaarNumber;

    @Valid
    @NotEmpty(message = "At least one address is required")
    private List<AddressRequest> addresses;

    @Valid
    @NotEmpty(message = "At least one employment detail is required")
    private List<EmploymentRequest> employmentDetails;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddressRequest {
        @NotNull(message = "Address type is required")
        private Address.AddressType addressType;

        @NotBlank(message = "Address line 1 is required")
        @Size(max = 500, message = "Address line 1 must not exceed 500 characters")
        private String addressLine1;

        @Size(max = 500, message = "Address line 2 must not exceed 500 characters")
        private String addressLine2;

        @NotBlank(message = "City is required")
        @Size(max = 100, message = "City must not exceed 100 characters")
        private String city;

        @NotBlank(message = "State is required")
        @Size(max = 100, message = "State must not exceed 100 characters")
        private String state;

        @NotBlank(message = "Postal code is required")
        @Pattern(regexp = "^[0-9]{5,10}$", message = "Postal code must be 5-10 digits")
        private String postalCode;

        @NotBlank(message = "Country is required")
        @Size(max = 100, message = "Country must not exceed 100 characters")
        private String country;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmploymentRequest {
        @NotNull(message = "Employment type is required")
        private EmploymentDetails.EmploymentType employmentType;

        @Size(max = 200, message = "Employer name must not exceed 200 characters")
        private String employerName;

        @Size(max = 500, message = "Employer address must not exceed 500 characters")
        private String employerAddress;

        @Size(max = 20, message = "Designation must not exceed 20 characters")
        private String designation;

        @Size(max = 20, message = "Department must not exceed 20 characters")
        private String department;

        @NotNull(message = "Monthly income is required")
        @Positive(message = "Monthly income must be positive")
        private java.math.BigDecimal monthlyIncome;

        @Size(max = 20, message = "Employment ID must not exceed 20 characters")
        private String employmentId;

        private LocalDate employmentStartDate;

        private LocalDate employmentEndDate;

        @Size(max = 500, message = "Work location must not exceed 500 characters")
        private String workLocation;

        @Size(max = 20, message = "Industry must not exceed 20 characters")
        private String industry;

        @NotNull(message = "Current employment flag is required")
        private Boolean currentEmployment;
    }
}
