package com.pm.triageservice.payloads.events;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class SymptomEvent {
    private String event_id;
    private String patient_id;
    private String district;
    private String clinic_id;
    private List<Map<String, Object>> symptoms;
    private String timestamp;
}
