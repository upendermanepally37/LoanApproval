package com.bank.loanapproval.customerservice.dto;

import com.bank.loanapproval.domain.customer.Document;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentResponse {
    private Long id;
    private Document.DocumentType documentType;
    private String documentName;
    private String documentPath;
    private String documentNumber;
    private String documentFormat;
    private Long fileSize;
    private Document.DocumentStatus status;
    private String verificationRemarks;
    private LocalDateTime verifiedAt;
    private String verifiedBy;
    private LocalDateTime uploadedAt;
    private String uploadedBy;
}
