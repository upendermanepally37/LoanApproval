package com.bank.loanapproval.loanapplicationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableFeignClients
@EnableJpaAuditing
@EntityScan(basePackages = {"com.bank.loanapproval.domain", "com.bank.loanapproval.loanapplicationservice"})
@ComponentScan(basePackages = {"com.bank.loanapproval.common", "com.bank.loanapproval.loanapplicationservice"})
public class LoanApplicationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoanApplicationServiceApplication.class, args);
    }
}
