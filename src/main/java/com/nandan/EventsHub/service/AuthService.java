package com.nandan.EventsHub.service;

import com.nandan.EventsHub.dto.LoginRequest;
import com.nandan.EventsHub.dto.LoginResponse;
import com.nandan.EventsHub.exception.InvalidCredentialsException;
import com.nandan.EventsHub.model.User;
import com.nandan.EventsHub.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid username or password"));

        // In production, you should hash passwords. For simplicity, direct comparison here
        if (!user.getPassword().equals(request.getPassword())) {
            throw new InvalidCredentialsException("Invalid username or password");
        }

        return new LoginResponse(true, "Login successful");
    }
}
