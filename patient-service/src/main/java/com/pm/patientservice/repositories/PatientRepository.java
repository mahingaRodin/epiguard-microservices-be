package com.pm.patientservice.repositories;

import com.pm.patientservice.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PatientRepository extends JpaRepository<Patient, UUID> {

}
