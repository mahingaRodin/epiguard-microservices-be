package com.pm.patientservice.services;

import com.pm.patientservice.models.Patient;
import com.pm.patientservice.payloads.requests.PatientRequest;
import com.pm.patientservice.repositories.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatientService {
    public final PatientRepository patientRepository;

    public Patient createPatient(PatientRequest request) {
        Patient patient = new Patient().builder()
                .age(request.getAge())
                .gender(request.getGender())
                .district(request.getDistrict())
                .build();
        return patientRepository.save(patient);
    }
}
