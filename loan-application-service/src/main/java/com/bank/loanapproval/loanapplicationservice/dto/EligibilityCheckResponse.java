package com.bank.loanapproval.loanapplicationservice.dto;

import com.bank.loanapproval.common.enumeration.EligibilityStatus;
import com.bank.loanapproval.domain.loanapplication.EligibilityCheck;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EligibilityCheckResponse {
    private Long id;
    private EligibilityCheck.CheckType checkType;
    private String checkName;
    private String checkDescription;
    private BigDecimal actualValue;
    private BigDecimal requiredValue;
    private String stringValue;
    private EligibilityStatus status;
    private String remarks;
    private Boolean passed;
    private LocalDateTime checkedAt;
    private String checkedBy;
}
