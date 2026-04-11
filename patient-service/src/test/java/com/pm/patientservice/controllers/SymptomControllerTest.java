package com.pm.patientservice.controllers;

import com.pm.patientservice.payloads.requests.SymptomRequest;
import com.pm.patientservice.services.SymptomService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SymptomController.class)
@AutoConfigureMockMvc(addFilters = false)
class SymptomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SymptomService symptomService;

    @Test
    void logSymptoms_ShouldReturn202AndCallServiceWithEmail() throws Exception {
        mockMvc.perform(post("/api/symptoms")
                        .header("X-User-Email", "clinic@test.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "patientId": "550e8400-e29b-41d4-a716-446655440000",
                                  "symptoms": [
                                    {
                                      "type": "FEVER",
                                      "severity": "HIGH"
                                    }
                                  ]
                                }
                                """))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.status").value("accepted"))
                .andExpect(jsonPath("$.message").value("Symptoms logged and queued for Outbreak Analysis."))
                .andExpect(jsonPath("$.event_id").isNotEmpty());

        verify(symptomService).addSymptoms(any(SymptomRequest.class), eq("clinic@test.com"));
    }

    @Test
    void logSymptoms_ShouldReturn202AndAllowMissingEmailHeader() throws Exception {
        mockMvc.perform(post("/api/symptoms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "patientId": "550e8400-e29b-41d4-a716-446655440000",
                                  "symptoms": [
                                    {
                                      "type": "COUGH",
                                      "severity": "LOW"
                                    }
                                  ]
                                }
                                """))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.status").value("accepted"))
                .andExpect(jsonPath("$.event_id").isNotEmpty());

        verify(symptomService).addSymptoms(any(SymptomRequest.class), eq(null));
    }
}