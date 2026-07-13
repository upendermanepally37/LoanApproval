package com.bank.loanapproval.eligibilityengine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableFeignClients
@EnableJpaAuditing
@EnableAsync
@EntityScan(basePackages = {"com.bank.loanapproval.domain", "com.bank.loanapproval.eligibilityengine"})
@ComponentScan(basePackages = {"com.bank.loanapproval.common", "com.bank.loanapproval.eligibilityengine"})
public class EligibilityEngineApplication {

    public static void main(String[] args) {
        SpringApplication.run(EligibilityEngineApplication.class, args);
    }
}
