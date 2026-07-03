package com.bank.loanapproval.loanproductservice.service;

import com.bank.loanapproval.common.enumeration.LoanType;
import com.bank.loanapproval.common.exception.BusinessException;
import com.bank.loanapproval.common.exception.ResourceNotFoundException;
import com.bank.loanapproval.common.exception.ValidationException;
import com.bank.loanapproval.loanproductservice.dto.EligibilityRuleRequest;
import com.bank.loanapproval.loanproductservice.dto.LoanProductRequest;
import com.bank.loanapproval.loanproductservice.dto.LoanProductResponse;
import com.bank.loanapproval.loanproductservice.mapper.LoanProductMapper;
import com.bank.loanapproval.loanproductservice.repository.EligibilityRuleRepository;
import com.bank.loanapproval.loanproductservice.repository.LoanProductRepository;
import com.bank.loanapproval.domain.loanproduct.EligibilityRule;
import com.bank.loanapproval.domain.loanproduct.LoanProduct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoanProductService {

    private final LoanProductRepository loanProductRepository;
    private final EligibilityRuleRepository eligibilityRuleRepository;
    private final LoanProductMapper loanProductMapper;

    @Transactional
    public LoanProductResponse createLoanProduct(LoanProductRequest request) {
        log.info("Creating loan product with code: {}", request.getProductCode());

        validateLoanProductRequest(request);
        validateLoanProductConfiguration(request);

        LoanProduct loanProduct = loanProductMapper.toEntity(request);
        loanProduct.setActive(true);

        if (request.getEligibilityRules() != null && !request.getEligibilityRules().isEmpty()) {
            Set<EligibilityRule> rules = new HashSet<>();
            request.getEligibilityRules().forEach(ruleRequest -> {
                EligibilityRule rule = loanProductMapper.toEligibilityRuleEntity(ruleRequest);
                rule.setLoanProduct(loanProduct);
                rule.setActive(true);
                rules.add(rule);
            });
            loanProduct.setEligibilityRules(rules);
        }

        LoanProduct savedProduct = loanProductRepository.save(loanProduct);
        log.info("Loan product created successfully with code: {}", savedProduct.getProductCode());

        return loanProductMapper.toResponse(savedProduct);
    }

    @Transactional(readOnly = true)
    public LoanProductResponse getLoanProductByCode(String productCode) {
        log.info("Fetching loan product with code: {}", productCode);
        LoanProduct loanProduct = loanProductRepository.findByProductCode(productCode)
                .orElseThrow(() -> new ResourceNotFoundException("Loan Product", "productCode", productCode));
        return loanProductMapper.toResponse(loanProduct);
    }

    @Transactional(readOnly = true)
    public LoanProductResponse getLoanProductById(Long id) {
        log.info("Fetching loan product with id: {}", id);
        LoanProduct loanProduct = loanProductRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan Product", "id", id));
        return loanProductMapper.toResponse(loanProduct);
    }

    @Transactional(readOnly = true)
    public List<LoanProductResponse> getActiveProductsByType(LoanType loanType) {
        log.info("Fetching active loan products for type: {}", loanType);
        List<LoanProduct> products = loanProductRepository.findByLoanTypeAndActiveTrue(loanType);
        return loanProductMapper.toResponseList(products);
    }

    @Transactional(readOnly = true)
    public List<LoanProductResponse> getAllActiveProducts(LoanType loanType) {
        log.info("Fetching all active loan products");
        List<LoanProduct> products = loanProductRepository.findActiveProducts(loanType, java.time.LocalDateTime.now());
        return loanProductMapper.toResponseList(products);
    }

    @Transactional(readOnly = true)
    public Page<LoanProductResponse> searchLoanProducts(String productCode, String productName,
                                                         LoanType loanType, Boolean active, Pageable pageable) {
        log.info("Searching loan products with filters - productCode: {}, productName: {}, loanType: {}, active: {}",
                productCode, productName, loanType, active);
        Page<LoanProduct> products = loanProductRepository.searchLoanProducts(
                productCode, productName, loanType, active, pageable);
        return products.map(loanProductMapper::toResponse);
    }

    @Transactional
    public LoanProductResponse updateLoanProduct(String productCode, LoanProductRequest request) {
        log.info("Updating loan product with code: {}", productCode);
        LoanProduct existingProduct = loanProductRepository.findByProductCode(productCode)
                .orElseThrow(() -> new ResourceNotFoundException("Loan Product", "productCode", productCode));

        if (!existingProduct.getProductCode().equals(request.getProductCode())) {
            throw new ValidationException("Cannot change product code");
        }

        validateLoanProductConfiguration(request);

        loanProductMapper.updateEntityFromRequest(request, existingProduct);
        LoanProduct updatedProduct = loanProductRepository.save(existingProduct);
        log.info("Loan product updated successfully with code: {}", updatedProduct.getProductCode());

        return loanProductMapper.toResponse(updatedProduct);
    }

    @Transactional
    public void activateLoanProduct(String productCode) {
        log.info("Activating loan product: {}", productCode);
        LoanProduct loanProduct = loanProductRepository.findByProductCode(productCode)
                .orElseThrow(() -> new ResourceNotFoundException("Loan Product", "productCode", productCode));
        loanProduct.setActive(true);
        loanProductRepository.save(loanProduct);
        log.info("Loan product activated successfully: {}", productCode);
    }

    @Transactional
    public void deactivateLoanProduct(String productCode) {
        log.info("Deactivating loan product: {}", productCode);
        LoanProduct loanProduct = loanProductRepository.findByProductCode(productCode)
                .orElseThrow(() -> new ResourceNotFoundException("Loan Product", "productCode", productCode));
        loanProduct.setActive(false);
        loanProductRepository.save(loanProduct);
        log.info("Loan product deactivated successfully: {}", productCode);
    }

    private void validateLoanProductRequest(LoanProductRequest request) {
        if (loanProductRepository.existsByProductCode(request.getProductCode())) {
            throw new ValidationException("Product code already exists");
        }
    }

    private void validateLoanProductConfiguration(LoanProductRequest request) {
        if (request.getMaxTenureMonths() < request.getMinTenureMonths()) {
            throw new ValidationException("Maximum tenure must be greater than or equal to minimum tenure");
        }
        if (request.getMaxLoanAmount().compareTo(request.getMinLoanAmount()) < 0) {
            throw new ValidationException("Maximum loan amount must be greater than or equal to minimum loan amount");
        }
        if (request.getMaxAge() < request.getMinAge()) {
            throw new ValidationException("Maximum age must be greater than or equal to minimum age");
        }
        if (request.getProcessingFeePercentage().compareTo(java.math.BigDecimal.valueOf(100)) > 0) {
            throw new ValidationException("Processing fee percentage must not exceed 100%");
        }
    }
}
