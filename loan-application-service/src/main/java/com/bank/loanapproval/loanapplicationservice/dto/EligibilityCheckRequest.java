package com.bank.loanapproval.loanapplicationservice.dto;

import com.bank.loanapproval.common.enumeration.EligibilityStatus;
import com.bank.loanapproval.domain.loanapplication.EligibilityCheck;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EligibilityCheckRequest {

    @NotNull(message = "Check type is required")
    private EligibilityCheck.CheckType checkType;

    @NotBlank(message = "Check name is required")
    @Size(max = 100, message = "Check name must not exceed 100 characters")
    private String checkName;

    @Size(max = 500, message = "Check description must not exceed 500 characters")
    private String checkDescription;

    private BigDecimal actualValue;

    private BigDecimal requiredValue;

    @Size(max = 100, message = "String value must not exceed 100 characters")
    private String stringValue;

    @NotNull(message = "Status is required")
    private EligibilityStatus status;

    @Size(max = 500, message = "Remarks must not exceed 500 characters")
    private String remarks;

    private Boolean passed;
}
