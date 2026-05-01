package com.foodtrack.spring.springboot_application.entrypoint.user;

import com.foodtrack.spring.springboot_application.application.model.UserAccountView;
import org.springframework.stereotype.Component;

@Component
public class UserAdministrationMapper {
    public UserAccountResponse toResponse(UserAccountView view) {
        return new UserAccountResponse(
                view.id(),
                view.fullName(),
                view.email(),
                view.role().name()
        );
    }
}
