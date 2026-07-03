package com.bank.loanapproval.customerservice.controller;

import com.bank.loanapproval.common.dto.ApiResponse;
import com.bank.loanapproval.customerservice.dto.DocumentRequest;
import com.bank.loanapproval.customerservice.dto.DocumentResponse;
import com.bank.loanapproval.customerservice.service.DocumentService;
import com.bank.loanapproval.domain.customer.Document;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/{customerNumber}/documents")
@RequiredArgsConstructor
@Tag(name = "Document Management", description = "APIs for managing customer documents")
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping
    @Operation(summary = "Upload document", description = "Upload a document for a customer")
    public ResponseEntity<ApiResponse<DocumentResponse>> uploadDocument(
            @Parameter(description = "Customer number") @PathVariable String customerNumber,
            @Valid @RequestBody DocumentRequest request) {
        DocumentResponse response = documentService.uploadDocument(customerNumber, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(response));
    }

    @GetMapping
    @Operation(summary = "Get customer documents", description = "Retrieve all documents for a customer")
    public ResponseEntity<ApiResponse<List<DocumentResponse>>> getCustomerDocuments(
            @Parameter(description = "Customer number") @PathVariable String customerNumber) {
        List<DocumentResponse> documents = documentService.getCustomerDocuments(customerNumber);
        return ResponseEntity.ok(ApiResponse.success(documents));
    }

    @PatchMapping("/{documentId}/status")
    @Operation(summary = "Update document status", description = "Update verification status of a document")
    public ResponseEntity<ApiResponse<DocumentResponse>> updateDocumentStatus(
            @Parameter(description = "Document ID") @PathVariable Long documentId,
            @RequestParam Document.DocumentStatus status,
            @RequestParam(required = false) String remarks) {
        DocumentResponse response = documentService.updateDocumentStatus(documentId, status, remarks);
        return ResponseEntity.ok(ApiResponse.success("Document status updated successfully", response));
    }

    @DeleteMapping("/{documentId}")
    @Operation(summary = "Delete document", description = "Delete a document")
    public ResponseEntity<ApiResponse<Void>> deleteDocument(
            @Parameter(description = "Document ID") @PathVariable Long documentId) {
        documentService.deleteDocument(documentId);
        return ResponseEntity.ok(ApiResponse.success("Document deleted successfully", null));
    }
}
