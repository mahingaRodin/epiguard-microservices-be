package com.pm.patientservice.services;

import com.pm.patientservice.events.SymptomEvent;
import com.pm.patientservice.kafkaServices.KafkaProducerService;
import com.pm.patientservice.models.Patient;
import com.pm.patientservice.models.Symptom;
import com.pm.patientservice.payloads.requests.SymptomRequest;
import com.pm.patientservice.repositories.PatientRepository;
import com.pm.patientservice.repositories.SymptomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SymptomService {
    private final SymptomRepository symptomRepository;
    private final PatientRepository patientRepository;
    private final KafkaProducerService kafkaProducer;

    @Transactional
    public void addSymptoms(SymptomRequest request, String userEmail) {

        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found!"));

        List<Symptom> symptoms = request.getSymptoms().stream()
                .map(dto -> Symptom.builder()
                        .patient(patient)
                        .type(dto.getType())
                        .severity(dto.getSeverity())
                        .build())
                .collect(Collectors.toList());

        symptomRepository.saveAll(symptoms);
        SymptomEvent event = SymptomEvent.builder()
                .event_id(UUID.randomUUID().toString())
                .patient_id(patient.getId().toString())
                .district(patient.getDistrict())
                .clinic_id(userEmail != null ? userEmail : "UNKNOWN_CLINIC")
                .symptoms(request.getSymptoms())
                .timestamp(Instant.now().toString())
                .build();
        kafkaProducer.sendSymptomEvent(event);
    }
}
