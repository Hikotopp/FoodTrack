package com.foodtrack.spring.springboot_application.application.port.out;

import com.foodtrack.spring.springboot_application.domain.model.AppUser;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryPort {
    List<AppUser> findAll();
    Optional<AppUser> findById(Long id);
    Optional<AppUser> findByEmail(String email);
    boolean existsByEmail(String email);
    AppUser save(AppUser user);
    void deleteById(Long id);
    long count();
}
