package com.foodtrack.spring.springboot_application.entrypoint.user;

import com.foodtrack.spring.springboot_application.application.port.in.UserAdministrationUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Users")
@SecurityRequirement(name = "bearerAuth")
public class UserAdministrationController {

    private final UserAdministrationUseCase userAdministrationUseCase;
    private final UserAdministrationMapper userAdministrationMapper;

    public UserAdministrationController(
            UserAdministrationUseCase userAdministrationUseCase,
            UserAdministrationMapper userAdministrationMapper
    ) {
        this.userAdministrationUseCase = userAdministrationUseCase;
        this.userAdministrationMapper = userAdministrationMapper;
    }

    @GetMapping
    @Operation(summary = "List user accounts")
    public ResponseEntity<List<UserAccountResponse>> listUsers() {
        return ResponseEntity.ok(userAdministrationUseCase.listUsers().stream()
                .map(userAdministrationMapper::toResponse)
                .toList());
    }

    @PostMapping
    @Operation(summary = "Create a user account")
    public ResponseEntity<UserAccountResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userAdministrationMapper.toResponse(
                userAdministrationUseCase.createUser(
                        request.fullName(),
                        request.email(),
                        request.password(),
                        request.role()
                )
        ));
    }

    @PatchMapping("/{userId}/role")
    @Operation(summary = "Update user account role")
    public ResponseEntity<UserAccountResponse> updateRole(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateUserRoleRequest request
    ) {
        return ResponseEntity.ok(userAdministrationMapper.toResponse(
                userAdministrationUseCase.updateRole(userId, request.role())
        ));
    }
}
