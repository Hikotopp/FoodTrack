package com.foodtrack.spring.springboot_application.application.port.out;

import com.foodtrack.spring.springboot_application.domain.model.AppUser;

import java.util.Optional;

public interface UserRepositoryPort {
    Optional<AppUser> findByEmail(String email);
    boolean existsByEmail(String email);
    AppUser save(AppUser user);
    long count();
}
