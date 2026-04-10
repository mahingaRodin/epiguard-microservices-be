package com.pm.patientservice.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "symptoms")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Symptom {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    @JsonIgnore
    @ToString.Exclude
    private Patient patient;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private Integer severity;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
