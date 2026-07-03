package com.bank.loanapproval.customerservice.repository;

import com.bank.loanapproval.domain.customer.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByCustomerNumber(String customerNumber);

    Optional<Customer> findByEmail(String email);

    Optional<Customer> findByPhoneNumber(String phoneNumber);

    @Query("SELECT c FROM Customer c WHERE " +
           "(:firstName IS NULL OR LOWER(c.firstName) LIKE LOWER(CONCAT('%', :firstName, '%'))) AND " +
           "(:lastName IS NULL OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))) AND " +
           "(:email IS NULL OR LOWER(c.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND " +
           "(:phoneNumber IS NULL OR c.phoneNumber = :phoneNumber) AND " +
           "(:kycStatus IS NULL OR c.kycStatus = :kycStatus) AND " +
           "c.deleted = false")
    Page<Customer> searchCustomers(
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("email") String email,
            @Param("phoneNumber") String phoneNumber,
            @Param("kycStatus") com.bank.loanapproval.common.enumeration.KYCStatus kycStatus,
            Pageable pageable);

    @Query("SELECT c FROM Customer c WHERE c.panNumber = :panNumber AND c.deleted = false")
    Optional<Customer> findByPanNumber(@Param("panNumber") String panNumber);

    @Query("SELECT c FROM Customer c WHERE c.aadhaarNumber = :aadhaarNumber AND c.deleted = false")
    Optional<Customer> findByAadhaarNumber(@Param("aadhaarNumber") String aadhaarNumber);
}
