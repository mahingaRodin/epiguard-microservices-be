package com.pm.authservice.controller;

import com.pm.authservice.exception.UserException;
import com.pm.authservice.model.User;
import com.pm.authservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "Endpoints for managing user profiles and information")
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {
    private final UserService userService;

    @Operation(
            summary = "Get current user profile",
            description = "Retrieves the profile information of the currently authenticated user based on the provided JWT token"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User profile retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = User.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Invalid or missing JWT token",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found for the provided token",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content
            )
    })
    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfile(
            @Parameter(
                    description = "Bearer JWT token for authentication",
                    required = true,
                    example = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
                    schema = @Schema(type = "string", format = "Bearer [token]")
            )
            @RequestHeader("Authorization") String token
    ) {
        User user = userService.getCurrentUserFromToken(token);
        return ResponseEntity.ok(user);
    }

    @Operation(
            summary = "Get user by ID",
            description = "Retrieves user information by their unique identifier. Requires appropriate permissions."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = User.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid user ID format",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Invalid or missing JWT token",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - Insufficient permissions to view this user",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found with the given ID",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(
            @Parameter(
                    description = "Bearer JWT token for authentication",
                    required = true,
                    example = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
                    schema = @Schema(type = "string", format = "Bearer [token]")
            )
            @RequestHeader("Authorization") String token,

            @Parameter(
                    name = "id",
                    description = "UUID of the user to retrieve",
                    required = true,
                    example = "123e4567-e89b-12d3-a456-426614174000"
            )
            @PathVariable UUID id
    ) throws UserException {
        User user = userService.getUserById(id);
        if (user == null) {
            throw new UserException("User Not Found!");
        }
        return ResponseEntity.ok(user);
    }
}