package com.pm.patientservice.controllers;

import com.pm.patientservice.models.Patient;
import com.pm.patientservice.payloads.requests.PatientRequest;
import com.pm.patientservice.services.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {
    private final PatientService patientService;

    @PostMapping
    public ResponseEntity<?> registerPatient(@Valid @RequestBody PatientRequest request) {
        Patient savedPatient = patientService.createPatient(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "patient_id", savedPatient.getId(),
                "created_at", savedPatient.getCreatedAt()
        ));
    }
}
