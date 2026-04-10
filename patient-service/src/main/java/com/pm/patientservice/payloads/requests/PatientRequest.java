package com.pm.patientservice.payloads.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PatientRequest {
    @NotNull(message = "Age is required")
    @Min(value = 0, message = "Age cannot be negative")
    private Integer age;

    @NotBlank(message = "Gender is required")
    private String gender;

    @NotBlank(message = "District is required")
    private String district;
}
