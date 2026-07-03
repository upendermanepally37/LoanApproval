package com.bank.loanapproval.domain.loanproduct;

import com.bank.loanapproval.common.enumeration.LoanType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "loan_products", indexes = {
    @Index(name = "idx_loan_product_type", columnList = "loan_type"),
    @Index(name = "idx_loan_product_active", columnList = "active")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String productCode;

    @Column(nullable = false, length = 100)
    private String productName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private LoanType loanType;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal interestRate;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal processingFee;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal processingFeePercentage;

    @Column(nullable = false)
    private Integer minTenureMonths;

    @Column(nullable = false)
    private Integer maxTenureMonths;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal minLoanAmount;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal maxLoanAmount;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal minMonthlySalary;

    @Column(nullable = false)
    private Integer minCreditScore;

    @Column(nullable = false)
    private Integer minAge;

    @Column(nullable = false)
    private Integer maxAge;

    @Column(nullable = false)
    @Builder.Default
    private Boolean prepaymentAllowed = true;

    @Column(precision = 5, scale = 2)
    private BigDecimal prepaymentFee;

    @Column(nullable = false)
    @Builder.Default
    private Boolean partPaymentAllowed = false;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal debtToIncomeRatio;

    @Column(length = 20)
    private String repaymentFrequency;

    @Column(length = 1000)
    private String eligibilityCriteria;

    @Column(length = 1000)
    private String requiredDocuments;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;

    @Column(name = "effective_from", nullable = false)
    private LocalDateTime effectiveFrom;

    @Column(name = "effective_to")
    private LocalDateTime effectiveTo;

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

    @OneToMany(mappedBy = "loanProduct", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<EligibilityRule> eligibilityRules = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (effectiveFrom == null) {
            effectiveFrom = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public boolean isEffective() {
        LocalDateTime now = LocalDateTime.now();
        return active && 
               (effectiveFrom == null || !effectiveFrom.isAfter(now)) &&
               (effectiveTo == null || effectiveTo.isAfter(now));
    }
}
