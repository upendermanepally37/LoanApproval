package com.bank.loanapproval.common.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishEvent(String topic, String eventType, Object payload) {
        try {
            kafkaTemplate.send(topic, eventType, payload);
            log.info("Event published successfully: {} to topic: {}", eventType, topic);
        } catch (Exception e) {
            log.error("Failed to publish event: {} to topic: {}", eventType, topic, e);
        }
    }

    public void publishEvent(String topic, Object payload) {
        try {
            kafkaTemplate.send(topic, payload);
            log.info("Event published successfully to topic: {}", topic);
        } catch (Exception e) {
            log.error("Failed to publish event to topic: {}", topic, e);
        }
    }
}
