package com.pm.patientservice.events;

import com.pm.patientservice.payloads.dtos.SymptomItemDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SymptomEvent {
    private String event_id;
    private String patient_id;
    private String district;
    private String clinic_id;
    private List<SymptomItemDto> symptoms;
    private String timestamp;
}
