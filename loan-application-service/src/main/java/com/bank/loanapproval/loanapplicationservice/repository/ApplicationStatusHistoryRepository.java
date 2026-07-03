package com.bank.loanapproval.loanapplicationservice.repository;

import com.bank.loanapproval.domain.loanapplication.ApplicationStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationStatusHistoryRepository extends JpaRepository<ApplicationStatusHistory, Long> {
    List<ApplicationStatusHistory> findByLoanApplicationIdOrderByChangedAtDesc(Long loanApplicationId);
}
