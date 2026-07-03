package com.bank.loanapproval.loanproductservice.repository;

import com.bank.loanapproval.common.enumeration.LoanType;
import com.bank.loanapproval.domain.loanproduct.LoanProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface LoanProductRepository extends JpaRepository<LoanProduct, Long> {

    Optional<LoanProduct> findByProductCode(String productCode);

    List<LoanProduct> findByLoanTypeAndActiveTrue(LoanType loanType);

    @Query("SELECT lp FROM LoanProduct lp WHERE lp.active = true AND " +
           "(:loanType IS NULL OR lp.loanType = :loanType) AND " +
           "(lp.effectiveFrom IS NULL OR lp.effectiveFrom <= :currentDate) AND " +
           "(lp.effectiveTo IS NULL OR lp.effectiveTo > :currentDate)")
    List<LoanProduct> findActiveProducts(
            @Param("loanType") LoanType loanType,
            @Param("currentDate") LocalDateTime currentDate);

    @Query("SELECT lp FROM LoanProduct lp WHERE " +
           "(:productCode IS NULL OR LOWER(lp.productCode) LIKE LOWER(CONCAT('%', :productCode, '%'))) AND " +
           "(:productName IS NULL OR LOWER(lp.productName) LIKE LOWER(CONCAT('%', :productName, '%'))) AND " +
           "(:loanType IS NULL OR lp.loanType = :loanType) AND " +
           "(:active IS NULL OR lp.active = :active)")
    Page<LoanProduct> searchLoanProducts(
            @Param("productCode") String productCode,
            @Param("productName") String productName,
            @Param("loanType") LoanType loanType,
            @Param("active") Boolean active,
            Pageable pageable);

    boolean existsByProductCode(String productCode);
}
