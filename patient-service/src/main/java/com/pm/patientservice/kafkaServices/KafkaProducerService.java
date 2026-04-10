package com.pm.patientservice.kafkaServices;

import com.pm.patientservice.events.SymptomEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC = "symptom-events";

    public void sendSymptomEvent(SymptomEvent event) {
        log.info("Publishing symptom even for patient {} in district {}", event.getPatient_id(), event.getDistrict());

        kafkaTemplate.send(TOPIC, event.getDistrict(), event);
    }

}
