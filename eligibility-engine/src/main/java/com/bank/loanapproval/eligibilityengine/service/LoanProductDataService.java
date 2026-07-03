package com.bank.loanapproval.eligibilityengine.service;

import com.bank.loanapproval.eligibilityengine.dto.LoanProductInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoanProductDataService {

    private final RestTemplate restTemplate = new RestTemplate();

    public LoanProductInfo getLoanProductInfo(Long loanProductId) {
        String url = "http://loan-product-service/api/loan-products/" + loanProductId;
        log.info("Fetching loan product info from: {}", url);
        
        try {
            return restTemplate.getForObject(url, LoanProductInfo.class);
        } catch (Exception e) {
            log.error("Failed to fetch loan product info for product: {}", loanProductId, e);
            throw new RuntimeException("Failed to fetch loan product info", e);
        }
    }
}
