package com.pm.authservice.impl;

import com.pm.authservice.exception.UserException;
import com.pm.authservice.model.User;
import com.pm.authservice.repository.UserRepository;
import com.pm.authservice.security.JwtTokenProvider;
import com.pm.authservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Autowired
    private final UserRepository userRepo;
    private final JwtTokenProvider provider;

    @Override
    public User getCurrentUserFromToken(String token) throws UserException {
        String email = provider.getEmailFromToken(token);
        User user = userRepo.findByEmail(email);
        if (user == null) {
            throw new UserException("Invalid Token!");
        }
        return user;
    }

    @Override
    public User getCurrentUser() throws UserException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return getUserByEmail(email);
    }

    @Override
    public User getUserByEmail(String email) throws UserException {
        User user = userRepo.findByEmail(email);
        if (user == null) {
            throw new UserException("User Not Found!");
        }
        return user;
    }

    @Override
    public User getUserById(UUID id) throws UserException {
        return userRepo.findById(id).orElse(null);
    }

    @Override
    public Page<User> getAllUsers(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return userRepo.findAll(pageable);
    }
}
