package com.bank.loanapproval.common.event;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class EventPublisherConfig {

    @Bean
    @ConditionalOnBean(KafkaTemplate.class)
    public EventPublisher eventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        return new EventPublisher(kafkaTemplate);
    }

    @Bean
    @ConditionalOnMissingBean(EventPublisher.class)
    public EventPublisher noOpEventPublisher() {
        return new EventPublisher(null);
    }
}
