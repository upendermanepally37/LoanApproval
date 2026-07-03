package com.bank.loanapproval.loanproductservice.controller;

import com.bank.loanapproval.common.dto.ApiResponse;
import com.bank.loanapproval.common.dto.PageResponse;
import com.bank.loanapproval.common.enumeration.LoanType;
import com.bank.loanapproval.loanproductservice.dto.LoanProductRequest;
import com.bank.loanapproval.loanproductservice.dto.LoanProductResponse;
import com.bank.loanapproval.loanproductservice.service.LoanProductService;
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

import java.util.List;

@RestController
@RequestMapping("/loan-products")
@RequiredArgsConstructor
@Tag(name = "Loan Product Management", description = "APIs for managing loan products")
public class LoanProductController {

    private final LoanProductService loanProductService;

    @PostMapping
    @Operation(summary = "Create a new loan product", description = "Create a new loan product with eligibility rules")
    public ResponseEntity<ApiResponse<LoanProductResponse>> createLoanProduct(
            @Valid @RequestBody LoanProductRequest request) {
        LoanProductResponse response = loanProductService.createLoanProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(response));
    }

    @GetMapping("/code/{productCode}")
    @Operation(summary = "Get loan product by code", description = "Retrieve loan product details using product code")
    public ResponseEntity<ApiResponse<LoanProductResponse>> getLoanProductByCode(
            @Parameter(description = "Product code") @PathVariable String productCode) {
        LoanProductResponse response = loanProductService.getLoanProductByCode(productCode);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get loan product by ID", description = "Retrieve loan product details using product ID")
    public ResponseEntity<ApiResponse<LoanProductResponse>> getLoanProductById(
            @Parameter(description = "Product ID") @PathVariable Long id) {
        LoanProductResponse response = loanProductService.getLoanProductById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/type/{loanType}")
    @Operation(summary = "Get active products by type", description = "Retrieve all active loan products for a specific loan type")
    public ResponseEntity<ApiResponse<List<LoanProductResponse>>> getActiveProductsByType(
            @Parameter(description = "Loan type") @PathVariable LoanType loanType) {
        List<LoanProductResponse> products = loanProductService.getActiveProductsByType(loanType);
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    @GetMapping("/active")
    @Operation(summary = "Get all active products", description = "Retrieve all currently active loan products")
    public ResponseEntity<ApiResponse<List<LoanProductResponse>>> getAllActiveProducts(
            @Parameter(description = "Filter by loan type (optional)") @RequestParam(required = false) LoanType loanType) {
        List<LoanProductResponse> products = loanProductService.getAllActiveProducts(loanType);
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    @GetMapping("/search")
    @Operation(summary = "Search loan products", description = "Search loan products with various filters")
    public ResponseEntity<ApiResponse<PageResponse<LoanProductResponse>>> searchLoanProducts(
            @Parameter(description = "Product code") @RequestParam(required = false) String productCode,
            @Parameter(description = "Product name") @RequestParam(required = false) String productName,
            @Parameter(description = "Loan type") @RequestParam(required = false) LoanType loanType,
            @Parameter(description = "Active status") @RequestParam(required = false) Boolean active,
            @Parameter(description = "Page number (0-indexed)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "createdAt") String sort,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "desc") String direction) {

        Sort.Direction sortDirection = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));

        Page<LoanProductResponse> productPage = loanProductService.searchLoanProducts(
                productCode, productName, loanType, active, pageable);

        PageResponse<LoanProductResponse> pageResponse = PageResponse.of(
                productPage.getContent(),
                productPage.getNumber(),
                productPage.getSize(),
                productPage.getTotalElements()
        );

        return ResponseEntity.ok(ApiResponse.success(pageResponse));
    }

    @PutMapping("/{productCode}")
    @Operation(summary = "Update loan product", description = "Update loan product details")
    public ResponseEntity<ApiResponse<LoanProductResponse>> updateLoanProduct(
            @Parameter(description = "Product code") @PathVariable String productCode,
            @Valid @RequestBody LoanProductRequest request) {
        LoanProductResponse response = loanProductService.updateLoanProduct(productCode, request);
        return ResponseEntity.ok(ApiResponse.success("Loan product updated successfully", response));
    }

    @PatchMapping("/{productCode}/activate")
    @Operation(summary = "Activate loan product", description = "Activate a loan product")
    public ResponseEntity<ApiResponse<Void>> activateLoanProduct(
            @Parameter(description = "Product code") @PathVariable String productCode) {
        loanProductService.activateLoanProduct(productCode);
        return ResponseEntity.ok(ApiResponse.success("Loan product activated successfully", null));
    }

    @PatchMapping("/{productCode}/deactivate")
    @Operation(summary = "Deactivate loan product", description = "Deactivate a loan product")
    public ResponseEntity<ApiResponse<Void>> deactivateLoanProduct(
            @Parameter(description = "Product code") @PathVariable String productCode) {
        loanProductService.deactivateLoanProduct(productCode);
        return ResponseEntity.ok(ApiResponse.success("Loan product deactivated successfully", null));
    }
}
