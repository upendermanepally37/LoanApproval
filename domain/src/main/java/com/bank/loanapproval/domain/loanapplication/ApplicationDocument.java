package com.bank.loanapproval.domain.loanapplication;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "application_documents")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_application_id", nullable = false)
    private LoanApplication loanApplication;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private DocumentType documentType;

    @Column(nullable = false, length = 255)
    private String documentName;

    @Column(length = 500)
    private String documentPath;

    @Column(length = 50)
    private String documentNumber;

    @Column(length = 20)
    private String documentFormat;

    @Column(precision = 19, scale = 2)
    private Long fileSize;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private DocumentStatus status = DocumentStatus.PENDING;

    @Column(length = 500)
    private String verificationRemarks;

    @Column(name = "verified_at")
    private LocalDateTime verifiedAt;

    @Column(name = "verified_by", length = 100)
    private String verifiedBy;

    @Column(name = "uploaded_at", nullable = false)
    private LocalDateTime uploadedAt;

    @Column(name = "uploaded_by", length = 100)
    private String uploadedBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (uploadedAt == null) {
            uploadedAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum DocumentType {
        INCOME_PROOF,
        BANK_STATEMENT,
        ADDRESS_PROOF,
        IDENTITY_PROOF,
        EMPLOYMENT_PROOF,
        PROPERTY_DOCUMENTS,
        GUARANTOR_DOCUMENTS,
        COLLATERAL_DOCUMENTS,
        INCOME_TAX_RETURNS,
        FORM_16,
        SALARY_SLIPS,
        BUSINESS_REGISTRATION,
        OTHER
    }

    public enum DocumentStatus {
        PENDING,
        UNDER_REVIEW,
        VERIFIED,
        REJECTED,
        EXPIRED
    }
}
