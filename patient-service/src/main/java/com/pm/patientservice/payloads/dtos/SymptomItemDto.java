package com.pm.patientservice.payloads.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SymptomItemDto {
    @NotBlank(message = "Symptom type is required")
    private String type;

    @NotNull(message = "Severity is required")
    @Min(value = 1, message = "Severity must be at least 1")
    @Max(value = 5, message = "Severity cannot exceed 5")
    private Integer severity;
}
