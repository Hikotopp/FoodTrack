package com.foodtrack.spring.springboot_application.application.service;

import com.foodtrack.spring.springboot_application.application.model.UserAccountView;
import com.foodtrack.spring.springboot_application.application.port.in.UserAdministrationUseCase;
import com.foodtrack.spring.springboot_application.application.port.out.CustomerOrderRepositoryPort;
import com.foodtrack.spring.springboot_application.application.port.out.UserRepositoryPort;
import com.foodtrack.spring.springboot_application.domain.exception.BusinessRuleException;
import com.foodtrack.spring.springboot_application.domain.exception.ResourceNotFoundException;
import com.foodtrack.spring.springboot_application.domain.model.AppUser;
import com.foodtrack.spring.springboot_application.domain.model.UserRole;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Service
@Transactional
public class UserAdministrationApplicationService implements UserAdministrationUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final CustomerOrderRepositoryPort customerOrderRepositoryPort;
    private final PasswordEncoder passwordEncoder;

    public UserAdministrationApplicationService(
            UserRepositoryPort userRepositoryPort,
            CustomerOrderRepositoryPort customerOrderRepositoryPort,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepositoryPort = userRepositoryPort;
        this.customerOrderRepositoryPort = customerOrderRepositoryPort;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserAccountView createUser(String fullName, String email, String rawPassword, UserRole role) {
        String normalizedEmail = normalizeEmail(email);
        if (userRepositoryPort.existsByEmail(normalizedEmail)) {
            throw new BusinessRuleException("Email is already registered.");
        }

        AppUser saved = userRepositoryPort.save(new AppUser(
                null,
                fullName.trim(),
                normalizedEmail,
                passwordEncoder.encode(rawPassword),
                role
        ));
        return toView(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserAccountView> listUsers() {
        return userRepositoryPort.findAll().stream()
                .sorted(Comparator.comparing(AppUser::fullName, String.CASE_INSENSITIVE_ORDER))
                .map(this::toView)
                .toList();
    }

    @Override
    public UserAccountView updateRole(Long userId, UserRole role) {
        AppUser user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User was not found."));

        AppUser updated = userRepositoryPort.save(new AppUser(
                user.id(),
                user.fullName(),
                user.email(),
                user.passwordHash(),
                role
        ));
        return toView(updated);
    }

    @Override
    public void deleteUser(Long userId, String currentUserEmail) {
        AppUser user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User was not found."));

        if (user.email().equals(normalizeEmail(currentUserEmail))) {
            throw new BusinessRuleException("You cannot delete your own account.");
        }

        if (user.role() == UserRole.ADMIN && countAdmins() <= 1) {
            throw new BusinessRuleException("At least one administrator account must remain active.");
        }

        if (customerOrderRepositoryPort.existsByCreatedByUserId(userId)) {
            throw new BusinessRuleException("This user has sales history and cannot be deleted.");
        }

        userRepositoryPort.deleteById(userId);
    }

    private long countAdmins() {
        return userRepositoryPort.findAll().stream()
                .filter(user -> user.role() == UserRole.ADMIN)
                .count();
    }

    private UserAccountView toView(AppUser user) {
        return new UserAccountView(user.id(), user.fullName(), user.email(), user.role());
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
