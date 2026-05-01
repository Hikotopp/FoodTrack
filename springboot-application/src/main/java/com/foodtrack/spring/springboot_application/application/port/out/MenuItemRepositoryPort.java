package com.foodtrack.spring.springboot_application.application.port.out;

import com.foodtrack.spring.springboot_application.domain.model.MenuItem;

import java.util.List;
import java.util.Optional;

import org.springframework.lang.NonNull;

public interface MenuItemRepositoryPort {
    List<MenuItem> findAllActive();
    Optional<MenuItem> findById(@NonNull Long id);
    MenuItem save(MenuItem menuItem);
    long count();
}
