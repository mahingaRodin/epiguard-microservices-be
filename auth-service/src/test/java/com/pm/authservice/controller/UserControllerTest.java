package com.pm.authservice.controller;

import com.pm.authservice.model.User;
import com.pm.authservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false) // Bypasses the Spring Security filters for unit testing
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    private User mockUser;
    private UUID testUserId;

    @BeforeEach
    void setUp() {
        testUserId = UUID.randomUUID();
        mockUser = new User();
        mockUser.setId(testUserId);
        mockUser.setEmail("rodin@test.com");
        mockUser.setName("Rodin M.");
    }

    @Test
    void getUserProfile_ShouldReturn200AndUser() throws Exception {
        String mockToken = "Bearer fake-token";
        when(userService.getCurrentUserFromToken(anyString())).thenReturn(mockUser);
        mockMvc.perform(get("/api/users/profile")
                        .header("Authorization", mockToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testUserId.toString()))
                .andExpect(jsonPath("$.email").value("rodin@test.com"))
                .andExpect(jsonPath("$.name").value("Rodin M."));
    }

    @Test
    void getUserById_ShouldReturn200AndUser_WhenUserExists() throws Exception {
        String mockToken = "Bearer fake-token";
        when(userService.getUserById(eq(testUserId))).thenReturn(mockUser);
        mockMvc.perform(get("/api/users/" + testUserId)
                        .header("Authorization", mockToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testUserId.toString()))
                .andExpect(jsonPath("$.email").value("rodin@test.com"));
    }

    @Test
    void getUserById_ShouldReturn404_WhenUserDoesNotExist() throws Exception {
        String mockToken = "Bearer fake-token";
        UUID nonExistentId = UUID.randomUUID();
        when(userService.getUserById(eq(nonExistentId))).thenReturn(null);
        mockMvc.perform(get("/api/users/" + nonExistentId)
                        .header("Authorization", mockToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}