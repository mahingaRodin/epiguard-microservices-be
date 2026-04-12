package com.pm.triageservice.repositories;

import com.pm.triageservice.models.TriageResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TriageRepository extends JpaRepository<TriageResult, UUID> {
}
