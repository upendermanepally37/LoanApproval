package com.bank.loanapproval.domain.customer;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "documents")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Customer customer;

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
        AADHAAR_CARD,
        PAN_CARD,
        PASSPORT,
        DRIVING_LICENSE,
        VOTER_ID,
        INCOME_PROOF,
        BANK_STATEMENT,
        ADDRESS_PROOF,
        PHOTOGRAPH,
        SIGNATURE,
        EDUCATIONAL_CERTIFICATE,
        BUSINESS_REGISTRATION,
        TAX_RETURNS,
        PROPERTY_DOCUMENTS,
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
