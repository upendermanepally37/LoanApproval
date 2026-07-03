package com.bank.loanapproval.customerservice.service;

import com.bank.loanapproval.common.exception.ResourceNotFoundException;
import com.bank.loanapproval.customerservice.dto.DocumentRequest;
import com.bank.loanapproval.customerservice.dto.DocumentResponse;
import com.bank.loanapproval.customerservice.mapper.CustomerMapper;
import com.bank.loanapproval.customerservice.repository.CustomerRepository;
import com.bank.loanapproval.customerservice.repository.DocumentRepository;
import com.bank.loanapproval.domain.customer.Customer;
import com.bank.loanapproval.domain.customer.Document;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Transactional
    public DocumentResponse uploadDocument(String customerNumber, DocumentRequest request) {
        log.info("Uploading document for customer: {}", customerNumber);
        Customer customer = customerRepository.findByCustomerNumber(customerNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "customerNumber", customerNumber));

        Document document = customerMapper.toDocumentEntity(request);
        document.setCustomer(customer);
        document.setStatus(Document.DocumentStatus.PENDING);
        document.setUploadedAt(java.time.LocalDateTime.now());

        Document savedDocument = documentRepository.save(document);
        log.info("Document uploaded successfully with id: {}", savedDocument.getId());

        return customerMapper.toDocumentResponse(savedDocument);
    }

    @Transactional(readOnly = true)
    public List<DocumentResponse> getCustomerDocuments(String customerNumber) {
        log.info("Fetching documents for customer: {}", customerNumber);
        Customer customer = customerRepository.findByCustomerNumber(customerNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "customerNumber", customerNumber));
        List<Document> documents = documentRepository.findByCustomerId(customer.getId());
        return documents.stream()
                .map(customerMapper::toDocumentResponse)
                .toList();
    }

    @Transactional
    public DocumentResponse updateDocumentStatus(Long documentId, Document.DocumentStatus status, String remarks) {
        log.info("Updating document status for document id: {} to {}", documentId, status);
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document", "id", documentId));

        document.setStatus(status);
        document.setVerificationRemarks(remarks);
        if (status == Document.DocumentStatus.VERIFIED || status == Document.DocumentStatus.REJECTED) {
            document.setVerifiedAt(java.time.LocalDateTime.now());
        }

        Document updatedDocument = documentRepository.save(document);
        log.info("Document status updated successfully for document id: {}", documentId);

        return customerMapper.toDocumentResponse(updatedDocument);
    }

    @Transactional
    public void deleteDocument(Long documentId) {
        log.info("Deleting document with id: {}", documentId);
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document", "id", documentId));
        documentRepository.delete(document);
        log.info("Document deleted successfully with id: {}", documentId);
    }
}
