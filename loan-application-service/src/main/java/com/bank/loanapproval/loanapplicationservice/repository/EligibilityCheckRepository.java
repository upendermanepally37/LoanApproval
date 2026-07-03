package com.bank.loanapproval.loanapplicationservice.repository;

import com.bank.loanapproval.domain.loanapplication.EligibilityCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EligibilityCheckRepository extends JpaRepository<EligibilityCheck, Long> {
    List<EligibilityCheck> findByLoanApplicationId(Long loanApplicationId);
}
