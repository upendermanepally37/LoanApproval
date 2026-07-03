package com.bank.loanapproval.loanproductservice.repository;

import com.bank.loanapproval.domain.loanproduct.EligibilityRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EligibilityRuleRepository extends JpaRepository<EligibilityRule, Long> {
    List<EligibilityRule> findByLoanProductId(Long loanProductId);
    List<EligibilityRule> findByLoanProductIdAndActiveTrueOrderByPriorityAsc(Long loanProductId);
}
