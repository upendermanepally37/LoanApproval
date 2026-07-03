package com.bank.loanapproval.customerservice.controller;

import com.bank.loanapproval.common.dto.ApiResponse;
import com.bank.loanapproval.common.dto.PageResponse;
import com.bank.loanapproval.common.enumeration.KYCStatus;
import com.bank.loanapproval.customerservice.dto.CustomerRequest;
import com.bank.loanapproval.customerservice.dto.CustomerResponse;
import com.bank.loanapproval.customerservice.service.CustomerService;
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
@RequestMapping("/customers")
@RequiredArgsConstructor
@Tag(name = "Customer Management", description = "APIs for managing customers")
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    @Operation(summary = "Register a new customer", description = "Create a new customer profile with personal details, addresses, and employment information")
    public ResponseEntity<ApiResponse<CustomerResponse>> createCustomer(@Valid @RequestBody CustomerRequest request) {
        CustomerResponse response = customerService.createCustomer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(response));
    }

    @GetMapping("/{customerNumber}")
    @Operation(summary = "Get customer by customer number", description = "Retrieve customer details using customer number")
    public ResponseEntity<ApiResponse<CustomerResponse>> getCustomerByNumber(
            @Parameter(description = "Customer number") @PathVariable String customerNumber) {
        CustomerResponse response = customerService.getCustomerByNumber(customerNumber);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/id/{id}")
    @Operation(summary = "Get customer by ID", description = "Retrieve customer details using customer ID")
    public ResponseEntity<ApiResponse<CustomerResponse>> getCustomerById(
            @Parameter(description = "Customer ID") @PathVariable Long id) {
        CustomerResponse response = customerService.getCustomerById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/search")
    @Operation(summary = "Search customers", description = "Search customers with various filters")
    public ResponseEntity<ApiResponse<PageResponse<CustomerResponse>>> searchCustomers(
            @Parameter(description = "First name") @RequestParam(required = false) String firstName,
            @Parameter(description = "Last name") @RequestParam(required = false) String lastName,
            @Parameter(description = "Email") @RequestParam(required = false) String email,
            @Parameter(description = "Phone number") @RequestParam(required = false) String phoneNumber,
            @Parameter(description = "KYC status") @RequestParam(required = false) KYCStatus kycStatus,
            @Parameter(description = "Page number (0-indexed)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "createdAt") String sort,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "desc") String direction) {

        Sort.Direction sortDirection = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));

        Page<CustomerResponse> customerPage = customerService.searchCustomers(
                firstName, lastName, email, phoneNumber, kycStatus, pageable);

        PageResponse<CustomerResponse> pageResponse = PageResponse.of(
                customerPage.getContent(),
                customerPage.getNumber(),
                customerPage.getSize(),
                customerPage.getTotalElements()
        );

        return ResponseEntity.ok(ApiResponse.success(pageResponse));
    }

    @PutMapping("/{customerNumber}")
    @Operation(summary = "Update customer", description = "Update customer details (only allowed before KYC verification)")
    public ResponseEntity<ApiResponse<CustomerResponse>> updateCustomer(
            @Parameter(description = "Customer number") @PathVariable String customerNumber,
            @Valid @RequestBody CustomerRequest request) {
        CustomerResponse response = customerService.updateCustomer(customerNumber, request);
        return ResponseEntity.ok(ApiResponse.success("Customer updated successfully", response));
    }

    @PatchMapping("/{customerNumber}/kyc")
    @Operation(summary = "Update KYC status", description = "Update KYC verification status for a customer")
    public ResponseEntity<ApiResponse<Void>> updateKYCStatus(
            @Parameter(description = "Customer number") @PathVariable String customerNumber,
            @RequestParam KYCStatus kycStatus,
            @RequestParam(required = false) String remarks) {
        customerService.updateKYCStatus(customerNumber, kycStatus, remarks);
        return ResponseEntity.ok(ApiResponse.success("KYC status updated successfully", null));
    }

    @PatchMapping("/{customerNumber}/deactivate")
    @Operation(summary = "Deactivate customer", description = "Deactivate a customer account")
    public ResponseEntity<ApiResponse<Void>> deactivateCustomer(
            @Parameter(description = "Customer number") @PathVariable String customerNumber) {
        customerService.deactivateCustomer(customerNumber);
        return ResponseEntity.ok(ApiResponse.success("Customer deactivated successfully", null));
    }
}
