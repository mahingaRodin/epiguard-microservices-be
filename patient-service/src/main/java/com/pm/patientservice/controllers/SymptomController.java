package com.pm.patientservice.controllers;

import com.pm.patientservice.payloads.requests.SymptomRequest;
import com.pm.patientservice.services.SymptomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/symptoms")
@RequiredArgsConstructor
public class SymptomController {

    private final SymptomService symptomService;

    @PostMapping
    public ResponseEntity<?> logSymptoms(
            @Valid @RequestBody SymptomRequest request,
            @RequestHeader(value = "X-User-Email", required = false) String userEmail) {

        symptomService.addSymptoms(request, userEmail);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(Map.of(
                "status", "accepted",
                "event_id", UUID.randomUUID().toString(),
                "message", "Symptoms logged and queued for Outbreak Analysis."
        ));
    }
}