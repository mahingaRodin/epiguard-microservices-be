package com.pm.authservice.service;

import com.pm.authservice.payloads.request.LoginRequest;
import com.pm.authservice.payloads.request.RegisterRequest;
import com.pm.authservice.payloads.response.AuthResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest registerRequest);
    AuthResponse login(LoginRequest loginRequest);
}
