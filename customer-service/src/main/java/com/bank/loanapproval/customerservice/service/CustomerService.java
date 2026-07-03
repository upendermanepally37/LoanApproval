package com.bank.loanapproval.customerservice.service;

import com.bank.loanapproval.common.enumeration.KYCStatus;
import com.bank.loanapproval.common.exception.BusinessException;
import com.bank.loanapproval.common.exception.ResourceNotFoundException;
import com.bank.loanapproval.common.exception.ValidationException;
import com.bank.loanapproval.customerservice.dto.CustomerRequest;
import com.bank.loanapproval.customerservice.dto.CustomerResponse;
import com.bank.loanapproval.customerservice.mapper.CustomerMapper;
import com.bank.loanapproval.customerservice.repository.CustomerRepository;
import com.bank.loanapproval.domain.customer.Address;
import com.bank.loanapproval.domain.customer.Customer;
import com.bank.loanapproval.domain.customer.EmploymentDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Transactional
    public CustomerResponse createCustomer(CustomerRequest request) {
        log.info("Creating customer with email: {}", request.getEmail());

        validateCustomerRequest(request);

        Customer customer = customerMapper.toEntity(request);
        customer.setCustomerNumber(generateCustomerNumber());
        customer.setKycStatus(KYCStatus.NOT_STARTED);
        customer.setActive(true);

        Set<Address> addresses = new HashSet<>();
        request.getAddresses().forEach(addressRequest -> {
            Address address = customerMapper.toAddressEntity(addressRequest);
            address.setCustomer(customer);
            addresses.add(address);
        });
        customer.setAddresses(addresses);

        Set<EmploymentDetails> employmentDetails = new HashSet<>();
        request.getEmploymentDetails().forEach(employmentRequest -> {
            EmploymentDetails employment = customerMapper.toEmploymentEntity(employmentRequest);
            employment.setCustomer(customer);
            employment.setAnnualIncome(employment.getMonthlyIncome().multiply(java.math.BigDecimal.valueOf(12)));
            employmentDetails.add(employment);
        });
        customer.setEmploymentDetails(employmentDetails);

        Customer savedCustomer = customerRepository.save(customer);
        log.info("Customer created successfully with customer number: {}", savedCustomer.getCustomerNumber());

        return customerMapper.toResponse(savedCustomer);
    }

    @Transactional(readOnly = true)
    public CustomerResponse getCustomerByNumber(String customerNumber) {
        log.info("Fetching customer with number: {}", customerNumber);
        Customer customer = customerRepository.findByCustomerNumber(customerNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "customerNumber", customerNumber));
        return customerMapper.toResponse(customer);
    }

    @Transactional(readOnly = true)
    public CustomerResponse getCustomerById(Long id) {
        log.info("Fetching customer with id: {}", id);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id));
        return customerMapper.toResponse(customer);
    }

    @Transactional(readOnly = true)
    public Page<CustomerResponse> searchCustomers(String firstName, String lastName, String email,
                                                    String phoneNumber, KYCStatus kycStatus, Pageable pageable) {
        log.info("Searching customers with filters - firstName: {}, lastName: {}, email: {}, phoneNumber: {}, kycStatus: {}",
                firstName, lastName, email, phoneNumber, kycStatus);
        Page<Customer> customers = customerRepository.searchCustomers(firstName, lastName, email, phoneNumber, kycStatus, pageable);
        return customers.map(customerMapper::toResponse);
    }

    @Transactional
    public CustomerResponse updateCustomer(String customerNumber, CustomerRequest request) {
        log.info("Updating customer with number: {}", customerNumber);
        Customer existingCustomer = customerRepository.findByCustomerNumber(customerNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "customerNumber", customerNumber));

        if (existingCustomer.getKycStatus() == KYCStatus.VERIFIED) {
            throw new BusinessException("Cannot update customer details after KYC verification");
        }

        customerMapper.updateEntityFromRequest(request, existingCustomer);
        Customer updatedCustomer = customerRepository.save(existingCustomer);
        log.info("Customer updated successfully with customer number: {}", updatedCustomer.getCustomerNumber());

        return customerMapper.toResponse(updatedCustomer);
    }

    @Transactional
    public void updateKYCStatus(String customerNumber, KYCStatus kycStatus, String remarks) {
        log.info("Updating KYC status for customer: {} to {}", customerNumber, kycStatus);
        Customer customer = customerRepository.findByCustomerNumber(customerNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "customerNumber", customerNumber));

        validateKYCStatusTransition(customer.getKycStatus(), kycStatus);

        customer.setKycStatus(kycStatus);
        customer.setKycRemarks(remarks);
        customerRepository.save(customer);
        log.info("KYC status updated successfully for customer: {}", customerNumber);
    }

    @Transactional
    public void deactivateCustomer(String customerNumber) {
        log.info("Deactivating customer: {}", customerNumber);
        Customer customer = customerRepository.findByCustomerNumber(customerNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "customerNumber", customerNumber));
        customer.setActive(false);
        customerRepository.save(customer);
        log.info("Customer deactivated successfully: {}", customerNumber);
    }

    private void validateCustomerRequest(CustomerRequest request) {
        if (customerRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ValidationException("Email already registered");
        }
        if (customerRepository.findByPhoneNumber(request.getPhoneNumber()).isPresent()) {
            throw new ValidationException("Phone number already registered");
        }
        if (request.getPanNumber() != null && customerRepository.findByPanNumber(request.getPanNumber()).isPresent()) {
            throw new ValidationException("PAN number already registered");
        }
        if (request.getAadhaarNumber() != null && customerRepository.findByAadhaarNumber(request.getAadhaarNumber()).isPresent()) {
            throw new ValidationException("Aadhaar number already registered");
        }
    }

    private void validateKYCStatusTransition(KYCStatus currentStatus, KYCStatus newStatus) {
        if (currentStatus == KYCStatus.VERIFIED && newStatus != KYCStatus.VERIFIED) {
            throw new BusinessException("Cannot change KYC status from VERIFIED to another status");
        }
    }

    private String generateCustomerNumber() {
        String prefix = "CUST";
        long timestamp = System.currentTimeMillis();
        return prefix + String.valueOf(timestamp).substring(String.valueOf(timestamp).length() - 7);
    }
}
