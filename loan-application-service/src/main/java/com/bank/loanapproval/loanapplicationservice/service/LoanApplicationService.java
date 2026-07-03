package com.bank.loanapproval.loanapplicationservice.service;

import com.bank.loanapproval.common.enumeration.ApplicationStatus;
import com.bank.loanapproval.common.event.EventPublisher;
import com.bank.loanapproval.common.exception.BusinessException;
import com.bank.loanapproval.common.exception.ResourceNotFoundException;
import com.bank.loanapproval.common.exception.ValidationException;
import com.bank.loanapproval.common.util.NumberGenerator;
import com.bank.loanapproval.loanapplicationservice.dto.*;
import com.bank.loanapproval.loanapplicationservice.mapper.LoanApplicationMapper;
import com.bank.loanapproval.loanapplicationservice.repository.*;
import com.bank.loanapproval.domain.customer.Customer;
import com.bank.loanapproval.domain.loanapplication.*;
import com.bank.loanapproval.domain.loanproduct.LoanProduct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoanApplicationService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final ApplicationDocumentRepository documentRepository;
    private final EligibilityCheckRepository eligibilityCheckRepository;
    private final ApplicationStatusHistoryRepository statusHistoryRepository;
    private final LoanApplicationMapper loanApplicationMapper;
    private final NumberGenerator numberGenerator;
    private final EventPublisher eventPublisher;
    private final RestTemplate restTemplate;

    @Transactional
    public LoanApplicationResponse createLoanApplication(LoanApplicationRequest request) {
        log.info("Creating loan application for customer: {}", request.getCustomerId());

        validateLoanApplicationRequest(request);
        Customer customer = fetchCustomer(request.getCustomerId());
        LoanProduct loanProduct = fetchLoanProduct(request.getLoanProductId());

        validateLoanRequestAgainstProduct(request, loanProduct, customer);

        LoanApplication loanApplication = loanApplicationMapper.toEntity(request);
        loanApplication.setApplicationNumber(numberGenerator.generateLoanApplicationNumber());
        loanApplication.setCustomer(customer);
        loanApplication.setLoanProduct(loanProduct);
        loanApplication.setStatus(ApplicationStatus.DRAFT);
        loanApplication.setFraudFlag(false);

        LoanApplication savedApplication = loanApplicationRepository.save(loanApplication);
        log.info("Loan application created successfully with number: {}", savedApplication.getApplicationNumber());

        eventPublisher.publishEvent("loan-application-events", "application.created", savedApplication);
        return loanApplicationMapper.toResponse(savedApplication);
    }

    @Transactional(readOnly = true)
    public LoanApplicationResponse getLoanApplicationByNumber(String applicationNumber) {
        log.info("Fetching loan application with number: {}", applicationNumber);
        LoanApplication loanApplication = loanApplicationRepository.findByApplicationNumber(applicationNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Loan Application", "applicationNumber", applicationNumber));
        return loanApplicationMapper.toResponse(loanApplication);
    }

    @Transactional(readOnly = true)
    public LoanApplicationResponse getLoanApplicationById(Long id) {
        log.info("Fetching loan application with id: {}", id);
        LoanApplication loanApplication = loanApplicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan Application", "id", id));
        return loanApplicationMapper.toResponse(loanApplication);
    }

    @Transactional(readOnly = true)
    public Page<LoanApplicationResponse> searchLoanApplications(String applicationNumber, Long customerId,
                                                                Long loanProductId, ApplicationStatus status,
                                                                Pageable pageable) {
        log.info("Searching loan applications with filters");
        Page<LoanApplication> applications = loanApplicationRepository.searchLoanApplications(
                applicationNumber, customerId, loanProductId, status, pageable);
        return applications.map(loanApplicationMapper::toResponse);
    }

    @Transactional
    public LoanApplicationResponse submitLoanApplication(String applicationNumber) {
        log.info("Submitting loan application: {}", applicationNumber);
        LoanApplication loanApplication = loanApplicationRepository.findByApplicationNumber(applicationNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Loan Application", "applicationNumber", applicationNumber));

        if (loanApplication.getStatus() != ApplicationStatus.DRAFT) {
            throw new BusinessException("Only draft applications can be submitted");
        }

        updateApplicationStatus(loanApplication, ApplicationStatus.SUBMITTED, "Application submitted by customer");
        loanApplication.setSubmittedAt(LocalDateTime.now());

        LoanApplication savedApplication = loanApplicationRepository.save(loanApplication);
        log.info("Loan application submitted successfully: {}", applicationNumber);

        eventPublisher.publishEvent("loan-application-events", "application.submitted", savedApplication);
        return loanApplicationMapper.toResponse(savedApplication);
    }

    @Transactional
    public LoanApplicationResponse updateApplicationStatus(String applicationNumber, StatusUpdateRequest request) {
        log.info("Updating loan application status: {} to {}", applicationNumber, request.getStatus());
        LoanApplication loanApplication = loanApplicationRepository.findByApplicationNumber(applicationNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Loan Application", "applicationNumber", applicationNumber));

        if (!loanApplication.canTransitionTo(request.getStatus())) {
            throw new BusinessException("Invalid status transition from " + 
                loanApplication.getStatus() + " to " + request.getStatus());
        }

        updateApplicationStatus(loanApplication, request.getStatus(), request.getRemarks());

        if (request.getStatus() == ApplicationStatus.APPROVED) {
            loanApplication.setApprovedAt(LocalDateTime.now());
            calculateLoanDetails(loanApplication);
        } else if (request.getStatus() == ApplicationStatus.REJECTED) {
            loanApplication.setRejectedAt(LocalDateTime.now());
            loanApplication.setRejectionReason(request.getRemarks());
        } else if (request.getStatus() == ApplicationStatus.DISBURSED) {
            loanApplication.setDisbursedAt(LocalDateTime.now());
        }

        LoanApplication savedApplication = loanApplicationRepository.save(loanApplication);
        log.info("Loan application status updated successfully: {}", applicationNumber);

        eventPublisher.publishEvent("loan-application-events", "application.status.updated", savedApplication);
        return loanApplicationMapper.toResponse(savedApplication);
    }

    @Transactional
    public LoanApplicationResponse updateLoanApplication(String applicationNumber, LoanApplicationRequest request) {
        log.info("Updating loan application: {}", applicationNumber);
        LoanApplication loanApplication = loanApplicationRepository.findByApplicationNumber(applicationNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Loan Application", "applicationNumber", applicationNumber));

        if (!loanApplication.isEditable()) {
            throw new BusinessException("Application cannot be updated in current status");
        }

        loanApplicationMapper.updateEntityFromRequest(request, loanApplication);
        LoanApplication savedApplication = loanApplicationRepository.save(loanApplication);
        log.info("Loan application updated successfully: {}", applicationNumber);

        return loanApplicationMapper.toResponse(savedApplication);
    }

    @Transactional
    public void withdrawLoanApplication(String applicationNumber) {
        log.info("Withdrawing loan application: {}", applicationNumber);
        LoanApplication loanApplication = loanApplicationRepository.findByApplicationNumber(applicationNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Loan Application", "applicationNumber", applicationNumber));

        if (!loanApplication.canTransitionTo(ApplicationStatus.WITHDRAWN)) {
            throw new BusinessException("Application cannot be withdrawn in current status");
        }

        updateApplicationStatus(loanApplication, ApplicationStatus.WITHDRAWN, "Application withdrawn by customer");
        loanApplicationRepository.save(loanApplication);
        log.info("Loan application withdrawn successfully: {}", applicationNumber);

        eventPublisher.publishEvent("loan-application-events", "application.withdrawn", loanApplication);
    }

    @Transactional
    public ApplicationDocumentResponse uploadDocument(String applicationNumber, ApplicationDocumentRequest request) {
        log.info("Uploading document for application: {}", applicationNumber);
        LoanApplication loanApplication = loanApplicationRepository.findByApplicationNumber(applicationNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Loan Application", "applicationNumber", applicationNumber));

        if (!loanApplication.isEditable()) {
            throw new BusinessException("Documents cannot be uploaded in current status");
        }

        ApplicationDocument document = loanApplicationMapper.toDocumentEntity(request);
        document.setLoanApplication(loanApplication);
        document.setStatus(ApplicationDocument.DocumentStatus.PENDING);
        document.setUploadedAt(LocalDateTime.now());

        ApplicationDocument savedDocument = documentRepository.save(document);
        log.info("Document uploaded successfully for application: {}", applicationNumber);

        return loanApplicationMapper.toDocumentResponse(savedDocument);
    }

    @Transactional
    public EligibilityCheckResponse addEligibilityCheck(String applicationNumber, EligibilityCheckRequest request) {
        log.info("Adding eligibility check for application: {}", applicationNumber);
        LoanApplication loanApplication = loanApplicationRepository.findByApplicationNumber(applicationNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Loan Application", "applicationNumber", applicationNumber));

        EligibilityCheck check = loanApplicationMapper.toEligibilityCheckEntity(request);
        check.setLoanApplication(loanApplication);
        check.setCheckedAt(LocalDateTime.now());

        EligibilityCheck savedCheck = eligibilityCheckRepository.save(check);
        log.info("Eligibility check added successfully for application: {}", applicationNumber);

        return loanApplicationMapper.toEligibilityCheckResponse(savedCheck);
    }

    private void validateLoanApplicationRequest(LoanApplicationRequest request) {
        if (loanApplicationRepository.existsByApplicationNumber(numberGenerator.generateLoanApplicationNumber())) {
            throw new ValidationException("Application number generation conflict");
        }
    }

    private Customer fetchCustomer(Long customerId) {
        return restTemplate.getForObject("http://customer-service/api/customers/id/" + customerId, Customer.class);
    }

    private LoanProduct fetchLoanProduct(Long loanProductId) {
        return restTemplate.getForObject("http://loan-product-service/api/loan-products/" + loanProductId, LoanProduct.class);
    }

    private void validateLoanRequestAgainstProduct(LoanApplicationRequest request, LoanProduct product, Customer customer) {
        if (request.getRequestedAmount().compareTo(product.getMinLoanAmount()) < 0 ||
            request.getRequestedAmount().compareTo(product.getMaxLoanAmount()) > 0) {
            throw new ValidationException("Requested amount is outside product limits");
        }

        if (request.getRequestedTenureMonths() < product.getMinTenureMonths() ||
            request.getRequestedTenureMonths() > product.getMaxTenureMonths()) {
            throw new ValidationException("Requested tenure is outside product limits");
        }

        if (customer.getAge() < product.getMinAge() || customer.getAge() > product.getMaxAge()) {
            throw new ValidationException("Customer age is outside product limits");
        }
    }

    private void updateApplicationStatus(LoanApplication loanApplication, ApplicationStatus newStatus, String remarks) {
        ApplicationStatusHistory history = ApplicationStatusHistory.builder()
                .loanApplication(loanApplication)
                .fromStatus(loanApplication.getStatus())
                .toStatus(newStatus)
                .remarks(remarks)
                .changedAt(LocalDateTime.now())
                .build();
        statusHistoryRepository.save(history);
        loanApplication.setStatus(newStatus);
    }

    private void calculateLoanDetails(LoanApplication loanApplication) {
        BigDecimal principal = loanApplication.getApprovedAmount() != null ? 
            loanApplication.getApprovedAmount() : loanApplication.getRequestedAmount();
        Integer tenure = loanApplication.getApprovedTenureMonths() != null ? 
            loanApplication.getApprovedTenureMonths() : loanApplication.getRequestedTenureMonths();
        BigDecimal rate = loanApplication.getLoanProduct().getInterestRate();

        BigDecimal monthlyRate = rate.divide(BigDecimal.valueOf(1200), 10, RoundingMode.HALF_UP);
        int months = tenure;
        
        BigDecimal emi = principal.multiply(monthlyRate)
                .multiply(BigDecimal.ONE.add(monthlyRate).pow(months))
                .divide(BigDecimal.ONE.add(monthlyRate).pow(months).subtract(BigDecimal.ONE), 2, RoundingMode.HALF_UP);

        loanApplication.setApprovedAmount(principal);
        loanApplication.setApprovedTenureMonths(tenure);
        loanApplication.setInterestRate(rate);
        loanApplication.setEmi(emi);
        loanApplication.setProcessingFee(principal.multiply(loanApplication.getLoanProduct().getProcessingFeePercentage())
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));

        LocalDate startDate = LocalDate.now().plusMonths(1);
        loanApplication.setRepaymentStartDate(startDate);
        loanApplication.setRepaymentEndDate(startDate.plusMonths(tenure));
    }
}
