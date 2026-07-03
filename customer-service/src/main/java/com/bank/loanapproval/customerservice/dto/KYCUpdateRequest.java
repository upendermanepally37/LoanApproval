package com.bank.loanapproval.customerservice.dto;

import com.bank.loanapproval.common.enumeration.KYCStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KYCUpdateRequest {

    @NotNull(message = "KYC status is required")
    private KYCStatus kycStatus;

    @Size(max = 500, message = "KYC remarks must not exceed 500 characters")
    private String kycRemarks;
}
