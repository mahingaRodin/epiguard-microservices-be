package com.pm.patientservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestKafka {
    @Autowired(required = false)
    private KafkaTemplate<Object, Object> kafkaTemplate;

    @GetMapping("/test-kafka")
    public String testKafka() {
        if (kafkaTemplate == null) {
            return "❌ KafkaTemplate is null - Kafka configuration failed";
        }

        try {
            kafkaTemplate.send("test-topic", "test", "Hello Kafka").get();
            return "✅ Kafka is working! Message sent successfully";
        } catch (Exception e) {
            return "❌ Kafka connection failed: " + e.getMessage();
        }
    }
}
