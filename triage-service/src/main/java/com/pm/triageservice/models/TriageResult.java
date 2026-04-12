package com.pm.triageservice.models;

import com.pm.triageservice.enums.EPriorityLevel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "triage_results")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TriageResult {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, name = "patient_id")
    private UUID patientId;

    @Column(nullable = false)
    private Double riskScore;

    @Column(nullable = false)
    private EPriorityLevel priorityLevel;

    @Column(name = "predicted_disease")
    private String predictedDisease;

    @Column(name = "doctor_override", nullable = false)
    @Builder.Default
    private Boolean doctorOverride = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

}
