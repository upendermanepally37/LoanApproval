package com.bank.loanapproval.customerservice.repository;

import com.bank.loanapproval.domain.customer.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByCustomerId(Long customerId);
    Optional<Document> findByCustomerIdAndDocumentType(Long customerId, Document.DocumentType documentType);
}
