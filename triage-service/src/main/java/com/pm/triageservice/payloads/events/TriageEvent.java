package com.pm.triageservice.payloads.events;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TriageEvent {
    private String event_id;
    private String patient_id;
    private String district;
    private Double risk_score;
    private String priority;
    private String predicted_disease;
    private String timestamp;
}
