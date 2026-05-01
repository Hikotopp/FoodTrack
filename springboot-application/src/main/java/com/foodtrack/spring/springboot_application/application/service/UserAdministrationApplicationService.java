package com.foodtrack.spring.springboot_application.application.service;

import com.foodtrack.spring.springboot_application.application.model.UserAccountView;
import com.foodtrack.spring.springboot_application.application.port.in.UserAdministrationUseCase;
import com.foodtrack.spring.springboot_application.application.port.out.UserRepositoryPort;
import com.foodtrack.spring.springboot_application.domain.exception.ResourceNotFoundException;
import com.foodtrack.spring.springboot_application.domain.model.AppUser;
import com.foodtrack.spring.springboot_application.domain.model.UserRole;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@Transactional
public class UserAdministrationApplicationService implements UserAdministrationUseCase {

    private final UserRepositoryPort userRepositoryPort;

    public UserAdministrationApplicationService(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
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

    private UserAccountView toView(AppUser user) {
        return new UserAccountView(user.id(), user.fullName(), user.email(), user.role());
    }
}
