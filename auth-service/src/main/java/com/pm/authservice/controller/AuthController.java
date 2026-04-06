package com.pm.authservice.controller;

import com.pm.authservice.exception.UserException;
import com.pm.authservice.model.User;
import com.pm.authservice.payloads.request.LoginRequest;
import com.pm.authservice.payloads.request.RegisterRequest;
import com.pm.authservice.payloads.response.AuthResponse;
import com.pm.authservice.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication management APIs for user register and login")
public class AuthController {
    private final AuthService authsService;

    @Operation(
            summary = "Register a new user",
            description = "Creates a new user account with the provided information. Returns authentication token upon successful registration."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User successfully registered",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input - User already exists or validation failed",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content
            )
    })
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerHandler(
            @Parameter(
                    description = "User registration details",
                    required = true,
                    schema = @Schema(implementation = User.class)
            )
            @Valid @RequestBody RegisterRequest registerRequest
    ) throws UserException {
        return ResponseEntity.ok(
                authsService.register(registerRequest)
        );
    }

    @Operation(
            summary = "User login",
            description = "Authenticates a user with email/username and password. Returns authentication token upon successful login."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully authenticated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid credentials - Wrong email/username or password",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content
            )
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginHandler(
            @Parameter(
                    description = "User login credentials (email/username and password)",
                    required = true,
                    schema = @Schema(implementation = User.class)
            )
            @Valid @RequestBody LoginRequest request
    ) throws UserException {
        return ResponseEntity.ok(
                authsService.login(request)
        );
    }
}