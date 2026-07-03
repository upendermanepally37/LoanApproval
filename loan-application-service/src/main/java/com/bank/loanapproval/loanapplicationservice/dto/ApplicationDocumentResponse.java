package com.bank.loanapproval.loanapplicationservice.dto;

import com.bank.loanapproval.domain.loanapplication.ApplicationDocument;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationDocumentResponse {
    private Long id;
    private ApplicationDocument.DocumentType documentType;
    private String documentName;
    private String documentPath;
    private String documentNumber;
    private String documentFormat;
    private Long fileSize;
    private ApplicationDocument.DocumentStatus status;
    private String verificationRemarks;
    private LocalDateTime verifiedAt;
    private String verifiedBy;
    private LocalDateTime uploadedAt;
    private String uploadedBy;
}
