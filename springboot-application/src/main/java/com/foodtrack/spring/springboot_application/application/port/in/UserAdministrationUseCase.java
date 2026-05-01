package com.foodtrack.spring.springboot_application.application.port.in;

import com.foodtrack.spring.springboot_application.application.model.UserAccountView;
import com.foodtrack.spring.springboot_application.domain.model.UserRole;

import java.util.List;

public interface UserAdministrationUseCase {
    List<UserAccountView> listUsers();
    UserAccountView updateRole(Long userId, UserRole role);
}
