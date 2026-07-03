package com.bank.loanapproval.domain.customer;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Entity
@Table(name = "employment_details")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmploymentDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Customer customer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EmploymentType employmentType;

    @Column(length = 200)
    private String employerName;

    @Column(length = 500)
    private String employerAddress;

    @Column(length = 20)
    private String designation;

    @Column(length = 20)
    private String department;

    @Column(precision = 19, scale = 2)
    private BigDecimal monthlyIncome;

    @Column(precision = 19, scale = 2)
    private BigDecimal annualIncome;

    @Column(length = 20)
    private String employmentId;

    private LocalDate employmentStartDate;

    private LocalDate employmentEndDate;

    @Column(length = 500)
    private String workLocation;

    @Column(length = 20)
    private String industry;

    @Column(nullable = false)
    @Builder.Default
    private Boolean currentEmployment = true;

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

    public enum EmploymentType {
        SALARIED,
        SELF_EMPLOYED,
        BUSINESS_OWNER,
        PROFESSIONAL,
        RETIRED,
        STUDENT,
        UNEMPLOYED
    }
}
