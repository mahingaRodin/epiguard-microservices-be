package com.pm.authservice.impl;

import com.pm.authservice.enums.ERole;
import com.pm.authservice.exception.UserException;
import com.pm.authservice.model.User;
import com.pm.authservice.payloads.request.LoginRequest;
import com.pm.authservice.payloads.request.RegisterRequest;
import com.pm.authservice.payloads.response.AuthResponse;
import com.pm.authservice.repository.UserRepository;
import com.pm.authservice.security.JwtTokenProvider;
import com.pm.authservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsImpl customUserDetails;
    private final JwtTokenProvider provider;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()) != null) {
            throw new UserException("Email already in use!");
        }
        User newUser = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole() != null ? request.getRole() : ERole.CHW)
                .clinicId(request.getClinicId())
                .build();
        User savedUser = userRepository.save(newUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = provider.generateToken(authentication);

        return AuthResponse.builder()
                .token(token)
                .message("Registered Successfully!")
                .user(savedUser)
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        log.info("Login Attempt for email: {}", request.getEmail());

        //validate input
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new UserException("Email cannot be empty");
        }
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new UserException("Password cannot be empty");
        }

        String email = request.getEmail().trim();
        String password = request.getPassword();

        Authentication authentication = authenticate(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = provider.generateToken(authentication);

        User user = userRepository.findByEmail(email);
        user.setLastLogin(LocalDateTime.now());
        user = userRepository.save(user);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(token);
        authResponse.setMessage("Logged In Successfully!");
        authResponse.setUser(user);

        log.info("Login successful for: {}", email);
        return authResponse;
    }

    private Authentication authenticate(String email, String password) {
        try {
            UserDetails userDetails = customUserDetails.loadUserByUsername(email);

            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                throw new UserException("Password doesn't match!");
            }

            return new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());

        } catch (UsernameNotFoundException e) {
            throw new UserException("Email doesn't exist: " + email);
        }
    }
}