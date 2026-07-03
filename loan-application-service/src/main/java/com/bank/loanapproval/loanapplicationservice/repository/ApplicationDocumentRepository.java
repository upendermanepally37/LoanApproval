package com.bank.loanapproval.loanapplicationservice.repository;

import com.bank.loanapproval.domain.loanapplication.ApplicationDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationDocumentRepository extends JpaRepository<ApplicationDocument, Long> {
    List<ApplicationDocument> findByLoanApplicationId(Long loanApplicationId);
}
