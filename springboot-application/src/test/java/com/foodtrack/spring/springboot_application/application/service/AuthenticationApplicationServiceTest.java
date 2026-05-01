package com.foodtrack.spring.springboot_application.application.service;

import com.foodtrack.spring.springboot_application.application.model.AuthenticationView;
import com.foodtrack.spring.springboot_application.application.port.out.UserRepositoryPort;
import com.foodtrack.spring.springboot_application.domain.model.AppUser;
import com.foodtrack.spring.springboot_application.domain.model.UserRole;
import com.foodtrack.spring.springboot_application.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthenticationApplicationService Unit Tests")
class AuthenticationApplicationServiceTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthenticationApplicationService authenticationService;

    private AppUser mockUser;
    private String testEmail = "test@example.com";
    private String testPassword = "SecurePassword123!";
    private String encodedPassword = "$2a$10$encoded";
    private String jwtToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...";

    @BeforeEach
    void setUp() {
        mockUser = new AppUser(1L, "Test User", testEmail, encodedPassword, UserRole.EMPLOYEE);
    }

    @Test
    @DisplayName("Should register a new user successfully")
    void testRegisterSuccess() {
        // Arrange
        when(userRepositoryPort.existsByEmail(testEmail)).thenReturn(false);
        when(passwordEncoder.encode(testPassword)).thenReturn(encodedPassword);
        when(userRepositoryPort.save(any(AppUser.class))).thenReturn(mockUser);
        when(jwtService.generateToken(mockUser)).thenReturn(jwtToken);

        // Act
        AuthenticationView result = authenticationService.register("Test User", testEmail, testPassword);

        // Assert
        assertNotNull(result);
        assertEquals(testEmail, result.email());
        verify(userRepositoryPort).existsByEmail(testEmail);
        verify(userRepositoryPort).save(any(AppUser.class));
    }

    @Test
    @DisplayName("Should login user successfully")
    void testLoginSuccess() {
        // Arrange
        when(userRepositoryPort.findByEmail(testEmail)).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(testPassword, encodedPassword)).thenReturn(true);
        when(jwtService.generateToken(mockUser)).thenReturn(jwtToken);

        // Act
        AuthenticationView result = authenticationService.login(testEmail, testPassword);

        // Assert
        assertNotNull(result);
        assertEquals(testEmail, result.email());
        verify(userRepositoryPort).findByEmail(testEmail);
    }
}
