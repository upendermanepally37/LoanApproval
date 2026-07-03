package com.bank.loanapproval.domain.loanapplication;

import com.bank.loanapproval.common.enumeration.EligibilityStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "eligibility_checks")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EligibilityCheck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_application_id", nullable = false)
    private LoanApplication loanApplication;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private CheckType checkType;

    @Column(nullable = false, length = 100)
    private String checkName;

    @Column(length = 500)
    private String checkDescription;

    @Column(precision = 19, scale = 2)
    private BigDecimal actualValue;

    @Column(precision = 19, scale = 2)
    private BigDecimal requiredValue;

    @Column(length = 100)
    private String stringValue;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EligibilityStatus status;

    @Column(length = 500)
    private String remarks;

    @Column(nullable = false)
    @Builder.Default
    private Boolean passed = false;

    @Column(name = "checked_at", nullable = false)
    private LocalDateTime checkedAt;

    @Column(name = "checked_by", length = 100)
    private String checkedBy;

    @PrePersist
    protected void onCreate() {
        if (checkedAt == null) {
            checkedAt = LocalDateTime.now();
        }
    }

    public enum CheckType {
        AGE_CHECK,
        SALARY_CHECK,
        EMPLOYMENT_CHECK,
        CREDIT_SCORE_CHECK,
        DEBT_TO_INCOME_RATIO_CHECK,
        LOAN_AMOUNT_CHECK,
        TENURE_CHECK,
        EMPLOYMENT_STABILITY_CHECK,
        RESIDENCE_STABILITY_CHECK,
        EXISTING_LOANS_CHECK,
        RISK_PROFILE_CHECK,
        FRAUD_CHECK,
        CUSTOM_RULE_CHECK
    }
}
