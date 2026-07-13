package com.bank.loanapproval.common.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;

@Slf4j
public class EventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public EventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishEvent(String topic, String eventType, Object payload) {
        if (kafkaTemplate == null) {
            log.debug("Kafka not available - skipping event publish: {} to topic: {}", eventType, topic);
            return;
        }
        try {
            kafkaTemplate.send(topic, eventType, payload);
            log.info("Event published successfully: {} to topic: {}", eventType, topic);
        } catch (Exception e) {
            log.error("Failed to publish event: {} to topic: {}", eventType, topic, e);
        }
    }

    public void publishEvent(String topic, Object payload) {
        if (kafkaTemplate == null) {
            log.debug("Kafka not available - skipping event publish to topic: {}", topic);
            return;
        }
        try {
            kafkaTemplate.send(topic, payload);
            log.info("Event published successfully to topic: {}", topic);
        } catch (Exception e) {
            log.error("Failed to publish event to topic: {}", topic, e);
        }
    }
}
