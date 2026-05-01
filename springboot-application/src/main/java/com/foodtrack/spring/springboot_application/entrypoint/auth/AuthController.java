package com.foodtrack.spring.springboot_application.entrypoint.auth;

import com.foodtrack.spring.springboot_application.application.model.AuthenticationView;
import com.foodtrack.spring.springboot_application.application.model.UserProfileView;
import com.foodtrack.spring.springboot_application.application.port.in.AuthUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication")
public class AuthController {

    private final AuthUseCase authUseCase;

    public AuthController(AuthUseCase authUseCase) {
        this.authUseCase = authUseCase;
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new employee account")
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthenticationView view = authUseCase.register(
                request.fullName(),
                request.email(),
                request.password()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(view));
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate a user and return a JWT token")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthenticationView view = authUseCase.login(request.email(), request.password());
        return ResponseEntity.ok(toResponse(view));
    }

    @GetMapping("/me")
    @Operation(summary = "Get the current authenticated user profile")
    public ResponseEntity<UserProfileView> me(Authentication authentication) {
        return ResponseEntity.ok(authUseCase.getProfile(authentication.getName()));
    }

    //  Mapper interno (simple y suficiente)
    private AuthenticationResponse toResponse(AuthenticationView view) {
        return new AuthenticationResponse(
                view.token(),
                view.fullName(),
                view.email(),
                view.role().name()
        );
    }
}