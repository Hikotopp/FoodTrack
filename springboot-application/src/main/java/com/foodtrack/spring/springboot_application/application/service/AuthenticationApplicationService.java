package com.foodtrack.spring.springboot_application.application.service;

import com.foodtrack.spring.springboot_application.application.model.AuthenticationView;
import com.foodtrack.spring.springboot_application.application.model.UserProfileView;
import com.foodtrack.spring.springboot_application.application.port.in.AuthUseCase;
import com.foodtrack.spring.springboot_application.application.port.out.UserRepositoryPort;
import com.foodtrack.spring.springboot_application.domain.exception.BusinessRuleException;
import com.foodtrack.spring.springboot_application.domain.exception.ResourceNotFoundException;
import com.foodtrack.spring.springboot_application.domain.model.AppUser;
import com.foodtrack.spring.springboot_application.domain.model.UserRole;
import com.foodtrack.spring.springboot_application.security.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
@Transactional
public class AuthenticationApplicationService implements AuthUseCase {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationApplicationService.class);

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthenticationApplicationService(
            UserRepositoryPort userRepositoryPort,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.userRepositoryPort = userRepositoryPort;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public AuthenticationView register(String fullName, String email, String rawPassword) {
        String normalizedEmail = normalizeEmail(email);
        logger.info("Register attempt for email: {}", normalizedEmail);
        
        if (userRepositoryPort.existsByEmail(normalizedEmail)) {
            logger.warn("Registration failed: email already exists: {}", normalizedEmail);
            throw new BusinessRuleException("Email is already registered.");
        }

        AppUser userToSave = new AppUser(
                null,
                fullName.trim(),
                normalizedEmail,
                passwordEncoder.encode(rawPassword),
                UserRole.EMPLOYEE
        );

        AppUser savedUser = userRepositoryPort.save(userToSave);
        logger.info("User registered successfully: {} (id: {})", normalizedEmail, savedUser.id());
        return toAuthenticationView(savedUser);
    }

    @Override
    public AuthenticationView login(String email, String rawPassword) {
        String normalizedEmail = normalizeEmail(email);
        logger.info("Login attempt for email: {}", normalizedEmail);
        
        AppUser user = userRepositoryPort.findByEmail(normalizedEmail)
                .orElseThrow(() -> {
                    logger.warn("Login failed: user not found: {}", normalizedEmail);
                    return new BusinessRuleException("Invalid credentials.");
                });

        if (!passwordEncoder.matches(rawPassword, user.passwordHash())) {
            logger.warn("Login failed: invalid password for email: {}", normalizedEmail);
            throw new BusinessRuleException("Invalid credentials.");
        }

        logger.info("User logged in successfully: {} (id: {})", normalizedEmail, user.id());
        return toAuthenticationView(user);
    }

    @Override
    public UserProfileView getProfile(String email) {
        String normalizedEmail = normalizeEmail(email);
        logger.debug("Fetching profile for email: {}", normalizedEmail);
        
        AppUser user = userRepositoryPort.findByEmail(normalizedEmail)
                .orElseThrow(() -> {
                    logger.error("Profile not found for email: {}", normalizedEmail);
                    return new ResourceNotFoundException("Authenticated user was not found.");
                });

        return new UserProfileView(user.id(), user.fullName(), user.email(), user.role());
    }

    private AuthenticationView toAuthenticationView(AppUser user) {
        return new AuthenticationView(
                jwtService.generateToken(user),
                user.fullName(),
                user.email(),
                user.role()
        );
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}