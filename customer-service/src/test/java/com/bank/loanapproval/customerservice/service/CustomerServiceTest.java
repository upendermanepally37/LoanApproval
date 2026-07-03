package com.bank.loanapproval.customerservice.service;

import com.bank.loanapproval.common.enumeration.KYCStatus;
import com.bank.loanapproval.common.enumeration.RiskProfile;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerService customerService;

    private Customer testCustomer;
    private CustomerRequest testRequest;
    private CustomerResponse testResponse;

    @BeforeEach
    void setUp() {
        testCustomer = createTestCustomer();
        testRequest = createTestRequest();
        testResponse = createTestResponse();
    }

    @Test
    void shouldCreateCustomerSuccessfully() {
        // Given
        when(customerRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(customerRepository.findByPhoneNumber(anyString())).thenReturn(Optional.empty());
        when(customerRepository.findByPanNumber(anyString())).thenReturn(Optional.empty());
        when(customerRepository.findByAadhaarNumber(anyString())).thenReturn(Optional.empty());
        when(customerMapper.toEntity(any(CustomerRequest.class))).thenReturn(testCustomer);
        when(customerMapper.toAddressEntity(any())).thenReturn(createTestAddress());
        when(customerMapper.toEmploymentEntity(any())).thenReturn(createTestEmploymentDetails());
        when(customerRepository.save(any(Customer.class))).thenReturn(testCustomer);
        when(customerMapper.toResponse(any(Customer.class))).thenReturn(testResponse);

        // When
        CustomerResponse result = customerService.createCustomer(testRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCustomerNumber()).isEqualTo(testResponse.getCustomerNumber());
        assertThat(result.getEmail()).isEqualTo(testResponse.getEmail());
        verify(customerRepository).save(any(Customer.class));
        verify(customerMapper).toResponse(any(Customer.class));
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyRegistered() {
        // Given
        when(customerRepository.findByEmail(anyString())).thenReturn(Optional.of(testCustomer));

        // When & Then
        assertThatThrownBy(() -> customerService.createCustomer(testRequest))
            .isInstanceOf(ValidationException.class)
            .hasMessageContaining("Email already registered");
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void shouldThrowExceptionWhenPhoneNumberAlreadyRegistered() {
        // Given
        when(customerRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(customerRepository.findByPhoneNumber(anyString())).thenReturn(Optional.of(testCustomer));

        // When & Then
        assertThatThrownBy(() -> customerService.createCustomer(testRequest))
            .isInstanceOf(ValidationException.class)
            .hasMessageContaining("Phone number already registered");
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void shouldThrowExceptionWhenPanNumberAlreadyRegistered() {
        // Given
        when(customerRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(customerRepository.findByPhoneNumber(anyString())).thenReturn(Optional.empty());
        when(customerRepository.findByPanNumber(anyString())).thenReturn(Optional.of(testCustomer));

        // When & Then
        assertThatThrownBy(() -> customerService.createCustomer(testRequest))
            .isInstanceOf(ValidationException.class)
            .hasMessageContaining("PAN number already registered");
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void shouldThrowExceptionWhenAadhaarNumberAlreadyRegistered() {
        // Given
        when(customerRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(customerRepository.findByPhoneNumber(anyString())).thenReturn(Optional.empty());
        when(customerRepository.findByPanNumber(anyString())).thenReturn(Optional.empty());
        when(customerRepository.findByAadhaarNumber(anyString())).thenReturn(Optional.of(testCustomer));

        // When & Then
        assertThatThrownBy(() -> customerService.createCustomer(testRequest))
            .isInstanceOf(ValidationException.class)
            .hasMessageContaining("Aadhaar number already registered");
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void shouldGetCustomerByNumberSuccessfully() {
        // Given
        String customerNumber = "CUST1234567";
        when(customerRepository.findByCustomerNumber(customerNumber)).thenReturn(Optional.of(testCustomer));
        when(customerMapper.toResponse(any(Customer.class))).thenReturn(testResponse);

        // When
        CustomerResponse result = customerService.getCustomerByNumber(customerNumber);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCustomerNumber()).isEqualTo(customerNumber);
        verify(customerRepository).findByCustomerNumber(customerNumber);
        verify(customerMapper).toResponse(testCustomer);
    }

    @Test
    void shouldThrowExceptionWhenCustomerNotFoundByNumber() {
        // Given
        String customerNumber = "CUST9999999";
        when(customerRepository.findByCustomerNumber(customerNumber)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> customerService.getCustomerByNumber(customerNumber))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("Customer")
            .hasMessageContaining(customerNumber);
    }

    @Test
    void shouldGetCustomerByIdSuccessfully() {
        // Given
        Long customerId = 1L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(testCustomer));
        when(customerMapper.toResponse(any(Customer.class))).thenReturn(testResponse);

        // When
        CustomerResponse result = customerService.getCustomerById(customerId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCustomerNumber()).isEqualTo(testResponse.getCustomerNumber());
        verify(customerRepository).findById(customerId);
        verify(customerMapper).toResponse(testCustomer);
    }

    @Test
    void shouldThrowExceptionWhenCustomerNotFoundById() {
        // Given
        Long customerId = 999L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> customerService.getCustomerById(customerId))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("Customer")
            .hasMessageContaining(customerId.toString());
    }

    @Test
    void shouldSearchCustomersSuccessfully() {
        // Given
        String firstName = "John";
        String lastName = "Doe";
        String email = "john.doe@example.com";
        String phoneNumber = "+1234567890";
        KYCStatus kycStatus = KYCStatus.VERIFIED;
        Pageable pageable = PageRequest.of(0, 10);
        
        Page<Customer> customerPage = new PageImpl<>(java.util.List.of(testCustomer), pageable, 1);
        Page<CustomerResponse> responsePage = new PageImpl<>(java.util.List.of(testResponse), pageable, 1);
        
        when(customerRepository.searchCustomers(firstName, lastName, email, phoneNumber, kycStatus, pageable))
            .thenReturn(customerPage);
        when(customerMapper.toResponse(testCustomer)).thenReturn(testResponse);

        // When
        Page<CustomerResponse> result = customerService.searchCustomers(
            firstName, lastName, email, phoneNumber, kycStatus, pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getCustomerNumber()).isEqualTo(testResponse.getCustomerNumber());
        verify(customerRepository).searchCustomers(firstName, lastName, email, phoneNumber, kycStatus, pageable);
    }

    @Test
    void shouldUpdateCustomerSuccessfully() {
        // Given
        String customerNumber = "CUST1234567";
        testCustomer.setKycStatus(KYCStatus.NOT_STARTED);
        when(customerRepository.findByCustomerNumber(customerNumber)).thenReturn(Optional.of(testCustomer));
        when(customerRepository.save(any(Customer.class))).thenReturn(testCustomer);
        when(customerMapper.toResponse(any(Customer.class))).thenReturn(testResponse);

        // When
        CustomerResponse result = customerService.updateCustomer(customerNumber, testRequest);

        // Then
        assertThat(result).isNotNull();
        verify(customerRepository).save(testCustomer);
        verify(customerMapper).updateEntityFromRequest(testRequest, testCustomer);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingVerifiedCustomer() {
        // Given
        String customerNumber = "CUST1234567";
        testCustomer.setKycStatus(KYCStatus.VERIFIED);
        when(customerRepository.findByCustomerNumber(customerNumber)).thenReturn(Optional.of(testCustomer));

        // When & Then
        assertThatThrownBy(() -> customerService.updateCustomer(customerNumber, testRequest))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("Cannot update customer details after KYC verification");
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentCustomer() {
        // Given
        String customerNumber = "CUST9999999";
        when(customerRepository.findByCustomerNumber(customerNumber)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> customerService.updateCustomer(customerNumber, testRequest))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("Customer")
            .hasMessageContaining(customerNumber);
    }

    @Test
    void shouldUpdateKYCStatusSuccessfully() {
        // Given
        String customerNumber = "CUST1234567";
        KYCStatus newStatus = KYCStatus.VERIFIED;
        String remarks = "KYC documents verified";
        testCustomer.setKycStatus(KYCStatus.PENDING_VERIFICATION);
        when(customerRepository.findByCustomerNumber(customerNumber)).thenReturn(Optional.of(testCustomer));
        when(customerRepository.save(any(Customer.class))).thenReturn(testCustomer);

        // When
        customerService.updateKYCStatus(customerNumber, newStatus, remarks);

        // Then
        verify(customerRepository).save(testCustomer);
        assertThat(testCustomer.getKycStatus()).isEqualTo(newStatus);
        assertThat(testCustomer.getKycRemarks()).isEqualTo(remarks);
    }

    @Test
    void shouldThrowExceptionWhenChangingKYCStatusFromVerified() {
        // Given
        String customerNumber = "CUST1234567";
        KYCStatus newStatus = KYCStatus.PENDING_VERIFICATION;
        testCustomer.setKycStatus(KYCStatus.VERIFIED);
        when(customerRepository.findByCustomerNumber(customerNumber)).thenReturn(Optional.of(testCustomer));

        // When & Then
        assertThatThrownBy(() -> customerService.updateKYCStatus(customerNumber, newStatus, "remarks"))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("Cannot change KYC status from VERIFIED to another status");
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void shouldDeactivateCustomerSuccessfully() {
        // Given
        String customerNumber = "CUST1234567";
        testCustomer.setActive(true);
        when(customerRepository.findByCustomerNumber(customerNumber)).thenReturn(Optional.of(testCustomer));
        when(customerRepository.save(any(Customer.class))).thenReturn(testCustomer);

        // When
        customerService.deactivateCustomer(customerNumber);

        // Then
        verify(customerRepository).save(testCustomer);
        assertThat(testCustomer.getActive()).isFalse();
    }

    @Test
    void shouldThrowExceptionWhenDeactivatingNonExistentCustomer() {
        // Given
        String customerNumber = "CUST9999999";
        when(customerRepository.findByCustomerNumber(customerNumber)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> customerService.deactivateCustomer(customerNumber))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("Customer")
            .hasMessageContaining(customerNumber);
    }

    private Customer createTestCustomer() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setCustomerNumber("CUST1234567");
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setEmail("john.doe@example.com");
        customer.setPhoneNumber("+1234567890");
        customer.setDateOfBirth(java.time.LocalDate.of(1990, 1, 1));
        customer.setGender("M");
        customer.setMaritalStatus("S");
        customer.setPanNumber("ABCDE1234F");
        customer.setAadhaarNumber("123456789012");
        customer.setKycStatus(KYCStatus.NOT_STARTED);
        customer.setActive(true);
        customer.setCreditScore("750");
        customer.setRiskProfile(RiskProfile.MEDIUM);
        customer.setAddresses(new HashSet<>());
        customer.setEmploymentDetails(new HashSet<>());
        return customer;
    }

    private CustomerRequest createTestRequest() {
        CustomerRequest.AddressRequest addressRequest = new CustomerRequest.AddressRequest();
        addressRequest.setAddressType(Address.AddressType.PERMANENT);
        addressRequest.setAddressLine1("123 Main St");
        addressRequest.setCity("New York");
        addressRequest.setState("NY");
        addressRequest.setPostalCode("10001");
        addressRequest.setCountry("USA");

        CustomerRequest.EmploymentRequest employmentRequest = new CustomerRequest.EmploymentRequest();
        employmentRequest.setEmploymentType(EmploymentDetails.EmploymentType.SALARIED);
        employmentRequest.setEmployerName("Tech Corp");
        employmentRequest.setMonthlyIncome(new BigDecimal("5000.00"));
        employmentRequest.setEmploymentStartDate(java.time.LocalDate.of(2020, 1, 1));
        employmentRequest.setCurrentEmployment(true);
        employmentRequest.setIndustry("Technology");

        java.util.List<CustomerRequest.AddressRequest> addresses = new java.util.ArrayList<>();
        addresses.add(addressRequest);

        java.util.List<CustomerRequest.EmploymentRequest> employmentDetails = new java.util.ArrayList<>();
        employmentDetails.add(employmentRequest);

        CustomerRequest request = new CustomerRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmail("john.doe@example.com");
        request.setPhoneNumber("+1234567890");
        request.setDateOfBirth(java.time.LocalDate.of(1990, 1, 1));
        request.setGender("M");
        request.setMaritalStatus("S");
        request.setPanNumber("ABCDE1234F");
        request.setAadhaarNumber("123456789012");
        request.setAddresses(addresses);
        request.setEmploymentDetails(employmentDetails);

        return request;
    }

    private CustomerResponse createTestResponse() {
        CustomerResponse response = new CustomerResponse();
        response.setId(1L);
        response.setCustomerNumber("CUST1234567");
        response.setFirstName("John");
        response.setLastName("Doe");
        response.setEmail("john.doe@example.com");
        response.setPhoneNumber("+1234567890");
        response.setDateOfBirth(java.time.LocalDate.of(1990, 1, 1));
        response.setGender("M");
        response.setMaritalStatus("S");
        response.setPanNumber("ABCDE1234F");
        response.setAadhaarNumber("123456789012");
        response.setKycStatus(KYCStatus.NOT_STARTED);
        response.setActive(true);
        response.setCreditScore("750");
        response.setRiskProfile(RiskProfile.MEDIUM);
        response.setAge(34);
        response.setFullName("John Doe");
        return response;
    }

    private Address createTestAddress() {
        Address address = new Address();
        address.setId(1L);
        address.setAddressType(Address.AddressType.PERMANENT);
        address.setAddressLine1("123 Main St");
        address.setCity("New York");
        address.setState("NY");
        address.setPostalCode("10001");
        address.setCountry("USA");
        return address;
    }

    private EmploymentDetails createTestEmploymentDetails() {
        EmploymentDetails employment = new EmploymentDetails();
        employment.setId(1L);
        employment.setEmploymentType(EmploymentDetails.EmploymentType.SALARIED);
        employment.setEmployerName("Tech Corp");
        employment.setMonthlyIncome(new BigDecimal("5000.00"));
        employment.setAnnualIncome(new BigDecimal("60000.00"));
        employment.setEmploymentStartDate(java.time.LocalDate.of(2020, 1, 1));
        employment.setCurrentEmployment(true);
        employment.setIndustry("Technology");
        return employment;
    }
}
