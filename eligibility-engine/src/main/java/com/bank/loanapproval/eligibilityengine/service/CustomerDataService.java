package com.bank.loanapproval.eligibilityengine.service;

import com.bank.loanapproval.eligibilityengine.dto.CustomerProfile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerDataService {

    private final RestTemplate restTemplate;

    public CustomerProfile getCustomerProfile(Long customerId) {
        String url = "http://customer-service/api/customers/id/" + customerId;
        log.info("Fetching customer profile from: {}", url);
        
        try {
            return restTemplate.getForObject(url, CustomerProfile.class);
        } catch (Exception e) {
            log.error("Failed to fetch customer profile for customer: {}", customerId, e);
            throw new RuntimeException("Failed to fetch customer profile", e);
        }
    }
}
