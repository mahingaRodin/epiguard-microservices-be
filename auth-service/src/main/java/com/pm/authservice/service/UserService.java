package com.pm.authservice.service;

import com.pm.authservice.model.User;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface UserService {
    User getCurrentUserFromToken(String token);
    User getCurrentUser();
    User getUserByEmail(String email);
    User getUserById(UUID id);
    Page<User> getAllUsers(int page, int size, String sortBy);
}
