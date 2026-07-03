package com.bank.loanapproval.loanapplicationservice.dto;

import com.bank.loanapproval.common.enumeration.ApplicationStatus;
import com.bank.loanapproval.common.enumeration.EligibilityStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanApplicationResponse {
    private Long id;
    private String applicationNumber;
    private Long customerId;
    private String customerName;
    private Long loanProductId;
    private String loanProductName;
    private BigDecimal requestedAmount;
    private BigDecimal approvedAmount;
    private Integer requestedTenureMonths;
    private Integer approvedTenureMonths;
    private BigDecimal interestRate;
    private BigDecimal processingFee;
    private BigDecimal emi;
    private LocalDate repaymentStartDate;
    private LocalDate repaymentEndDate;
    private ApplicationStatus status;
    private EligibilityStatus eligibilityStatus;
    private String rejectionReason;
    private String remarks;
    private Boolean fraudFlag;
    private String fraudRemarks;
    private LocalDateTime submittedAt;
    private LocalDateTime approvedAt;
    private LocalDateTime rejectedAt;
    private LocalDateTime disbursedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean editable;
    private Boolean finalStatus;
    private List<ApplicationDocumentResponse> documents;
    private List<EligibilityCheckResponse> eligibilityChecks;
}
