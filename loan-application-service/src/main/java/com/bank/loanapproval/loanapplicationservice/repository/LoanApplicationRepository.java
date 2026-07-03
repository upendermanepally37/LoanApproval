package com.bank.loanapproval.loanapplicationservice.repository;

import com.bank.loanapproval.common.enumeration.ApplicationStatus;
import com.bank.loanapproval.domain.loanapplication.LoanApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Long> {

    Optional<LoanApplication> findByApplicationNumber(String applicationNumber);

    List<LoanApplication> findByCustomerId(Long customerId);

    @Query("SELECT la FROM LoanApplication la WHERE " +
           "(:applicationNumber IS NULL OR LOWER(la.applicationNumber) LIKE LOWER(CONCAT('%', :applicationNumber, '%'))) AND " +
           "(:customerId IS NULL OR la.customer.id = :customerId) AND " +
           "(:loanProductId IS NULL OR la.loanProduct.id = :loanProductId) AND " +
           "(:status IS NULL OR la.status = :status)")
    Page<LoanApplication> searchLoanApplications(
            @Param("applicationNumber") String applicationNumber,
            @Param("customerId") Long customerId,
            @Param("loanProductId") Long loanProductId,
            @Param("status") ApplicationStatus status,
            Pageable pageable);

    @Query("SELECT COUNT(la) FROM LoanApplication la WHERE la.customer.id = :customerId AND la.status IN :statuses")
    long countByCustomerIdAndStatusIn(@Param("customerId") Long customerId, @Param("statuses") List<ApplicationStatus> statuses);

    boolean existsByApplicationNumber(String applicationNumber);
}
