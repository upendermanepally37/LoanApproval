package com.bank.loanapproval.loanapplicationservice.controller;

import com.bank.loanapproval.common.dto.ApiResponse;
import com.bank.loanapproval.common.dto.PageResponse;
import com.bank.loanapproval.common.enumeration.ApplicationStatus;
import com.bank.loanapproval.loanapplicationservice.dto.*;
import com.bank.loanapproval.loanapplicationservice.service.LoanApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/loan-applications")
@RequiredArgsConstructor
@Tag(name = "Loan Application Management", description = "APIs for managing loan applications")
public class LoanApplicationController {

    private final LoanApplicationService loanApplicationService;

    @PostMapping
    @Operation(summary = "Create a new loan application", description = "Create a new loan application for a customer")
    public ResponseEntity<ApiResponse<LoanApplicationResponse>> createLoanApplication(
            @Valid @RequestBody LoanApplicationRequest request) {
        LoanApplicationResponse response = loanApplicationService.createLoanApplication(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(response));
    }

    @GetMapping("/number/{applicationNumber}")
    @Operation(summary = "Get loan application by number", description = "Retrieve loan application details using application number")
    public ResponseEntity<ApiResponse<LoanApplicationResponse>> getLoanApplicationByNumber(
            @Parameter(description = "Application number") @PathVariable String applicationNumber) {
        LoanApplicationResponse response = loanApplicationService.getLoanApplicationByNumber(applicationNumber);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get loan application by ID", description = "Retrieve loan application details using application ID")
    public ResponseEntity<ApiResponse<LoanApplicationResponse>> getLoanApplicationById(
            @Parameter(description = "Application ID") @PathVariable Long id) {
        LoanApplicationResponse response = loanApplicationService.getLoanApplicationById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/search")
    @Operation(summary = "Search loan applications", description = "Search loan applications with various filters")
    public ResponseEntity<ApiResponse<PageResponse<LoanApplicationResponse>>> searchLoanApplications(
            @Parameter(description = "Application number") @RequestParam(required = false) String applicationNumber,
            @Parameter(description = "Customer ID") @RequestParam(required = false) Long customerId,
            @Parameter(description = "Loan product ID") @RequestParam(required = false) Long loanProductId,
            @Parameter(description = "Application status") @RequestParam(required = false) ApplicationStatus status,
            @Parameter(description = "Page number (0-indexed)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "createdAt") String sort,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "desc") String direction) {

        Sort.Direction sortDirection = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));

        Page<LoanApplicationResponse> applicationPage = loanApplicationService.searchLoanApplications(
                applicationNumber, customerId, loanProductId, status, pageable);

        PageResponse<LoanApplicationResponse> pageResponse = PageResponse.of(
                applicationPage.getContent(),
                applicationPage.getNumber(),
                applicationPage.getSize(),
                applicationPage.getTotalElements()
        );

        return ResponseEntity.ok(ApiResponse.success(pageResponse));
    }

    @PostMapping("/{applicationNumber}/submit")
    @Operation(summary = "Submit loan application", description = "Submit a draft loan application for review")
    public ResponseEntity<ApiResponse<LoanApplicationResponse>> submitLoanApplication(
            @Parameter(description = "Application number") @PathVariable String applicationNumber) {
        LoanApplicationResponse response = loanApplicationService.submitLoanApplication(applicationNumber);
        return ResponseEntity.ok(ApiResponse.success("Application submitted successfully", response));
    }

    @PutMapping("/{applicationNumber}")
    @Operation(summary = "Update loan application", description = "Update loan application details (only allowed in draft or documents required status)")
    public ResponseEntity<ApiResponse<LoanApplicationResponse>> updateLoanApplication(
            @Parameter(description = "Application number") @PathVariable String applicationNumber,
            @Valid @RequestBody LoanApplicationRequest request) {
        LoanApplicationResponse response = loanApplicationService.updateLoanApplication(applicationNumber, request);
        return ResponseEntity.ok(ApiResponse.success("Application updated successfully", response));
    }

    @PatchMapping("/{applicationNumber}/status")
    @Operation(summary = "Update application status", description = "Update the status of a loan application")
    public ResponseEntity<ApiResponse<LoanApplicationResponse>> updateApplicationStatus(
            @Parameter(description = "Application number") @PathVariable String applicationNumber,
            @Valid @RequestBody StatusUpdateRequest request) {
        LoanApplicationResponse response = loanApplicationService.updateApplicationStatus(applicationNumber, request);
        return ResponseEntity.ok(ApiResponse.success("Application status updated successfully", response));
    }

    @PatchMapping("/{applicationNumber}/withdraw")
    @Operation(summary = "Withdraw loan application", description = "Withdraw a loan application")
    public ResponseEntity<ApiResponse<Void>> withdrawLoanApplication(
            @Parameter(description = "Application number") @PathVariable String applicationNumber) {
        loanApplicationService.withdrawLoanApplication(applicationNumber);
        return ResponseEntity.ok(ApiResponse.success("Application withdrawn successfully", null));
    }

    @PostMapping("/{applicationNumber}/documents")
    @Operation(summary = "Upload document", description = "Upload a document for a loan application")
    public ResponseEntity<ApiResponse<ApplicationDocumentResponse>> uploadDocument(
            @Parameter(description = "Application number") @PathVariable String applicationNumber,
            @Valid @RequestBody ApplicationDocumentRequest request) {
        ApplicationDocumentResponse response = loanApplicationService.uploadDocument(applicationNumber, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(response));
    }

    @PostMapping("/{applicationNumber}/eligibility-checks")
    @Operation(summary = "Add eligibility check", description = "Add an eligibility check result for a loan application")
    public ResponseEntity<ApiResponse<EligibilityCheckResponse>> addEligibilityCheck(
            @Parameter(description = "Application number") @PathVariable String applicationNumber,
            @Valid @RequestBody EligibilityCheckRequest request) {
        EligibilityCheckResponse response = loanApplicationService.addEligibilityCheck(applicationNumber, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(response));
    }
}
