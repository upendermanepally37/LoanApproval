package com.bank.loanapproval.domain.loanapplication;

import com.bank.loanapproval.common.enumeration.ApplicationStatus;
import com.bank.loanapproval.common.enumeration.EligibilityStatus;
import com.bank.loanapproval.domain.customer.Customer;
import com.bank.loanapproval.domain.loanproduct.LoanProduct;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "loan_applications", indexes = {
    @Index(name = "idx_loan_app_customer", columnList = "customer_id"),
    @Index(name = "idx_loan_app_product", columnList = "loan_product_id"),
    @Index(name = "idx_loan_app_status", columnList = "status"),
    @Index(name = "idx_loan_app_number", columnList = "application_number")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String applicationNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_product_id", nullable = false)
    private LoanProduct loanProduct;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal requestedAmount;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal approvedAmount;

    @Column(nullable = false)
    private Integer requestedTenureMonths;

    @Column(nullable = false)
    private Integer approvedTenureMonths;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal interestRate;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal processingFee;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal emi;

    @Column(nullable = false)
    private LocalDate repaymentStartDate;

    @Column(nullable = false)
    private LocalDate repaymentEndDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private ApplicationStatus status = ApplicationStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EligibilityStatus eligibilityStatus;

    @Column(length = 1000)
    private String rejectionReason;

    @Column(length = 1000)
    private String remarks;

    @Column(nullable = false)
    @Builder.Default
    private Boolean fraudFlag = false;

    @Column(length = 500)
    private String fraudRemarks;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "rejected_at")
    private LocalDateTime rejectedAt;

    @Column(name = "disbursed_at")
    private LocalDateTime disbursedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    @Version
    private Long version;

    @OneToMany(mappedBy = "loanApplication", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<ApplicationDocument> documents = new HashSet<>();

    @OneToMany(mappedBy = "loanApplication", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<EligibilityCheck> eligibilityChecks = new HashSet<>();

    @OneToMany(mappedBy = "loanApplication", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<ApplicationStatusHistory> statusHistory = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public boolean canTransitionTo(ApplicationStatus newStatus) {
        return status.canTransitionTo(newStatus);
    }

    public boolean isEditable() {
        return status == ApplicationStatus.DRAFT || status == ApplicationStatus.DOCUMENTS_REQUIRED;
    }

    public boolean isFinalStatus() {
        return status.isFinalStatus();
    }
}
