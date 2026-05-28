package com.foodtrack.spring.springboot_application.application.service;

import com.foodtrack.spring.springboot_application.application.model.UserAccountView;
import com.foodtrack.spring.springboot_application.application.port.out.CustomerOrderRepositoryPort;
import com.foodtrack.spring.springboot_application.application.port.out.UserRepositoryPort;
import com.foodtrack.spring.springboot_application.domain.exception.BusinessRuleException;
import com.foodtrack.spring.springboot_application.domain.exception.ResourceNotFoundException;
import com.foodtrack.spring.springboot_application.domain.model.AppUser;
import com.foodtrack.spring.springboot_application.domain.model.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserAdministrationApplicationService Unit Tests")
class UserAdministrationApplicationServiceTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private CustomerOrderRepositoryPort customerOrderRepositoryPort;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserAdministrationApplicationService service;

    @Test
    void createUserNormalizesEmailEncodesPasswordAndReturnsView() {
        when(userRepositoryPort.existsByEmail("admin@example.com")).thenReturn(false);
        when(passwordEncoder.encode("Password123!")).thenReturn("hash");
        when(userRepositoryPort.save(any(AppUser.class))).thenReturn(user(1L, "Admin User", "admin@example.com", UserRole.ADMIN));

        UserAccountView result = service.createUser(" Admin User ", " ADMIN@EXAMPLE.COM ", "Password123!", UserRole.ADMIN);

        assertEquals(new UserAccountView(1L, "Admin User", "admin@example.com", UserRole.ADMIN), result);
        verify(userRepositoryPort).save(new AppUser(null, "Admin User", "admin@example.com", "hash", UserRole.ADMIN));
    }

    @Test
    void createUserRejectsDuplicateEmail() {
        when(userRepositoryPort.existsByEmail("taken@example.com")).thenReturn(true);

        assertThrows(BusinessRuleException.class,
                () -> service.createUser("Taken", "taken@example.com", "Password123!", UserRole.EMPLOYEE));
        verify(userRepositoryPort, never()).save(any());
    }

    @Test
    void listUsersSortsByFullNameIgnoringCase() {
        AppUser zoe = user(2L, "zoe", "zoe@example.com", UserRole.EMPLOYEE);
        AppUser ana = user(1L, "Ana", "ana@example.com", UserRole.ADMIN);
        when(userRepositoryPort.findAll()).thenReturn(List.of(zoe, ana));

        List<UserAccountView> result = service.listUsers();

        assertEquals(List.of(
                new UserAccountView(1L, "Ana", "ana@example.com", UserRole.ADMIN),
                new UserAccountView(2L, "zoe", "zoe@example.com", UserRole.EMPLOYEE)
        ), result);
    }

    @Test
    void updateRolePreservesIdentityFields() {
        when(userRepositoryPort.findById(2L)).thenReturn(Optional.of(user(2L, "Waiter", "waiter@example.com", UserRole.EMPLOYEE)));
        when(userRepositoryPort.save(any(AppUser.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserAccountView result = service.updateRole(2L, UserRole.ADMIN);

        assertEquals(new UserAccountView(2L, "Waiter", "waiter@example.com", UserRole.ADMIN), result);
    }

    @Test
    void deleteUserRejectsOwnAccountLastAdminAndUsersWithSalesHistory() {
        when(userRepositoryPort.findById(1L)).thenReturn(Optional.of(user(1L, "Admin", "admin@example.com", UserRole.ADMIN)));
        assertThrows(BusinessRuleException.class, () -> service.deleteUser(1L, "ADMIN@example.com"));

        when(userRepositoryPort.findById(2L)).thenReturn(Optional.of(user(2L, "Only Admin", "only@example.com", UserRole.ADMIN)));
        when(userRepositoryPort.findAll()).thenReturn(List.of(user(2L, "Only Admin", "only@example.com", UserRole.ADMIN)));
        assertThrows(BusinessRuleException.class, () -> service.deleteUser(2L, "other@example.com"));

        when(userRepositoryPort.findById(3L)).thenReturn(Optional.of(user(3L, "Employee", "employee@example.com", UserRole.EMPLOYEE)));
        when(customerOrderRepositoryPort.existsByCreatedByUserId(3L)).thenReturn(true);
        assertThrows(BusinessRuleException.class, () -> service.deleteUser(3L, "other@example.com"));
    }

    @Test
    void deleteUserDeletesEligibleUserAndThrowsWhenMissing() {
        when(userRepositoryPort.findById(4L)).thenReturn(Optional.of(user(4L, "Employee", "employee@example.com", UserRole.EMPLOYEE)));
        when(customerOrderRepositoryPort.existsByCreatedByUserId(4L)).thenReturn(false);

        service.deleteUser(4L, "admin@example.com");

        verify(userRepositoryPort).deleteById(4L);
        when(userRepositoryPort.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.deleteUser(99L, "admin@example.com"));
    }

    private AppUser user(Long id, String fullName, String email, UserRole role) {
        return new AppUser(id, fullName, email, "hash", role);
    }
}
