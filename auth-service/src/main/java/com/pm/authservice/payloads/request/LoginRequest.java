package com.pm.authservice.payloads.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
