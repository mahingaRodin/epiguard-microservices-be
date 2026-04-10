package com.pm.patientservice.payloads.requests;

import com.pm.patientservice.payloads.dtos.SymptomItemDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class SymptomRequest {
    @NotNull(message = "Patient ID is required")
    private UUID patientId;

    @NotEmpty(message = "At least one symptom must be provided")
    @Valid
    private List<SymptomItemDto> symptoms;
}
