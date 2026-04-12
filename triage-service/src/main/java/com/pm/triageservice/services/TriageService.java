package com.pm.triageservice.services;

import com.pm.triageservice.enums.EPriorityLevel;
import com.pm.triageservice.models.TriageResult;
import com.pm.triageservice.payloads.events.SymptomEvent;
import com.pm.triageservice.payloads.events.TriageEvent;
import com.pm.triageservice.repositories.TriageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TriageService {
    private final TriageRepository triageRepository;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private final KafkaTemplate<String, TriageEvent> kafkaTemplate;

    private static final String OUTGOING_TOPIC = "triage-events";

    @KafkaListener(topics = "symptom-events", groupId = "triage-group")
    @Transactional
    public void consumeSymptomEvent(SymptomEvent event) {
        log.info("Received symptom event for patient {} in district {}", event.getPatient_id(), event.getDistrict());

        Double calculatedRisk = Math.random();
        EPriorityLevel priority = calculatedRisk > 0.7 ? EPriorityLevel.HIGH : (calculatedRisk > 0.4 ? EPriorityLevel.MEDIUM : EPriorityLevel.LOW);
        String disease = calculatedRisk > 0.7 ? "Respiratory Infection" : "Unknown";

        TriageResult result = TriageResult.builder()
                .patientId(UUID.fromString(event.getPatient_id()))
                .riskScore(calculatedRisk)
                .priorityLevel(priority)
                .predictedDisease(disease)
                .build();

        triageRepository.save(result);
        log.info("Saved triage result for patient: {}", result.getPatientId());

        TriageEvent triageEvent = TriageEvent.builder()
                .event_id(UUID.randomUUID().toString())
                .patient_id(event.getPatient_id())
                .district(event.getDistrict())
                .risk_score(calculatedRisk)
                .priority(priority.name())
                .predicted_disease(disease)
                .timestamp(Instant.now().toString())
                .build();

        kafkaTemplate.send(OUTGOING_TOPIC, triageEvent.getDistrict(), triageEvent);
        log.info("Published triage-event to Kafka for district {}", triageEvent.getDistrict());
    }
}
