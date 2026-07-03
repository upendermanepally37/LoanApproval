package com.bank.loanapproval.customerservice.repository;

import com.bank.loanapproval.domain.customer.EmploymentDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmploymentDetailsRepository extends JpaRepository<EmploymentDetails, Long> {
    List<EmploymentDetails> findByCustomerId(Long customerId);
    List<EmploymentDetails> findByCustomerIdAndCurrentEmployment(Long customerId, Boolean currentEmployment);
}
