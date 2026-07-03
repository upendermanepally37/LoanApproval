package com.bank.loanapproval.domain.loanproduct;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "eligibility_rules")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EligibilityRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_product_id", nullable = false)
    private LoanProduct loanProduct;

    @Column(nullable = false, length = 100)
    private String ruleName;

    @Column(length = 500)
    private String ruleDescription;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private RuleType ruleType;

    @Column(length = 500)
    private String ruleExpression;

    @Column(precision = 19, scale = 2)
    private BigDecimal minValue;

    @Column(precision = 19, scale = 2)
    private BigDecimal maxValue;

    @Column(length = 100)
    private String stringValue;

    @Column(nullable = false)
    @Builder.Default
    private Integer priority = 0;

    @Column(nullable = false)
    @Builder.Default
    private Boolean mandatory = true;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum RuleType {
        AGE,
        SALARY,
        EMPLOYMENT_TYPE,
        CREDIT_SCORE,
        DEBT_TO_INCOME_RATIO,
        LOAN_AMOUNT,
        TENURE,
        EMPLOYMENT_STABILITY,
        RESIDENCE_STABILITY,
        EXISTING_LOANS,
        RISK_PROFILE,
        FRAUD_CHECK,
        CUSTOM_RULE
    }
}
