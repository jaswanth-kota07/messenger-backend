package com.jashu.Messenger.service;

import com.jashu.Messenger.dto.AuthResponse;
import com.jashu.Messenger.dto.LoginRequest;
import com.jashu.Messenger.dto.RegisterRequest;
import com.jashu.Messenger.dto.UserProfile;
import com.jashu.Messenger.exceptions.BadRequestException;
import com.jashu.Messenger.exceptions.ResourceAlreadyExistsException;
import com.jashu.Messenger.model.User;
import com.jashu.Messenger.repository.UserRepository;
import com.jashu.Messenger.security.JwtService;
import com.jashu.Messenger.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public void register(RegisterRequest request) {
        String username = normalizeUsername(request.getUsername());
        String email = normalizeEmail(request.getEmail());
        log.info("Attempting to register user: {} with email: {}", username, email);

        if (userRepository.existsByUsername(username)) {
            log.warn("Registration failed: Username {} already exists", username);
            throw new ResourceAlreadyExistsException("Username already exists");
        }

        if (userRepository.existsByEmail(email)) {
            log.warn("Registration failed: Email {} already exists", email);
            throw new ResourceAlreadyExistsException("Email already exists");
        }

        User user = new User(
                username,
                email,
                passwordEncoder.encode(request.getPassword())
        );
        userRepository.save(user);
        log.info("User registered successfully: {}", username);
    }

    public AuthResponse login(LoginRequest request) {
        String email = normalizeEmail(request.getEmail());
        log.info("Attempting login for email: {}", email);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, request.getPassword())
        );

        AuthResponse response = new AuthResponse();
        String token = "";

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        token = jwtService.generateToken(principal);

        UserProfile profile = new UserProfile(principal.getId(), principal.getAppUsername(), principal.getEmail());

        response.setToken(token);
        response.setUser(profile);

        log.info("Login successful for user: {}", principal.getAppUsername());
        return response;
    }

    private String normalizeUsername(String username) {
        if (username == null) {
            throw new BadRequestException("Username is required");
        }
        return username.trim().toLowerCase();
    }

    private String normalizeEmail(String email) {
        if (email == null) {
            throw new BadRequestException("Email is required");
        }
        return email.trim().toLowerCase();
    }
}
