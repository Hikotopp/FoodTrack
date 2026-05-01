package com.foodtrack.spring.springboot_application.application.port.in;

import com.foodtrack.spring.springboot_application.application.model.AuthenticationView;
import com.foodtrack.spring.springboot_application.application.model.UserProfileView;

public interface AuthUseCase {
    AuthenticationView register(String fullName, String email, String rawPassword);
    AuthenticationView login(String email, String rawPassword);
    UserProfileView getProfile(String email);
}
