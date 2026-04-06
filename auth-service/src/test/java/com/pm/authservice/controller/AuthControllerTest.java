package com.pm.authservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pm.authservice.model.User;
import com.pm.authservice.payloads.request.LoginRequest;
import com.pm.authservice.payloads.request.RegisterRequest;
import com.pm.authservice.payloads.response.AuthResponse;
import com.pm.authservice.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;
    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    private AuthResponse mockResponse;

    @BeforeEach
    void setUp() {
        User mockUser = new User();
        mockUser.setId(UUID.randomUUID());
        mockUser.setEmail("test@test.com");
        mockUser.setName("Test User");

        mockResponse = AuthResponse.builder()
                .token("mock-jwt-token")
                .message("Success")
                .user(mockUser)
                .build();
    }

    @Test
    void registerHandler_ShouldReturn200AndAuthResponse() throws Exception {
        // Arrange
        RegisterRequest request = RegisterRequest.builder()
                .name("Test User")
                .email("test@test.com")
                .password("password123")
                .build();

        when(authService.register(any(RegisterRequest.class))).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mock-jwt-token"))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.user.email").value("test@test.com"));
    }

    @Test
    void loginHandler_ShouldReturn200AndAuthResponse() throws Exception {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setEmail("test@test.com");
        request.setPassword("password123");

        when(authService.login(any(LoginRequest.class))).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mock-jwt-token"))
                .andExpect(jsonPath("$.message").value("Success"));
    }
}