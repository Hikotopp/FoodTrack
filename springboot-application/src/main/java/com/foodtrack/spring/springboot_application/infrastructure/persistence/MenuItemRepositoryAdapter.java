package com.foodtrack.spring.springboot_application.infrastructure.persistence;

import com.foodtrack.spring.springboot_application.application.port.out.MenuItemRepositoryPort;
import com.foodtrack.spring.springboot_application.domain.model.MenuItem;
import com.foodtrack.spring.springboot_application.infrastructure.persistence.mapper.MenuItemMapper;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class MenuItemRepositoryAdapter implements MenuItemRepositoryPort {

    private final JpaMenuItemRepository jpaMenuItemRepository;
    private final MenuItemMapper menuItemMapper;

    public MenuItemRepositoryAdapter(JpaMenuItemRepository jpaMenuItemRepository, MenuItemMapper menuItemMapper) {
        this.jpaMenuItemRepository = jpaMenuItemRepository;
        this.menuItemMapper = menuItemMapper;
    }

    @Override
    public List<MenuItem> findAllActive() {
        return jpaMenuItemRepository.findByActiveTrue().stream()
                .map(menuItemMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<MenuItem> findById(@NonNull Long id) {
        return jpaMenuItemRepository.findById(id).map(menuItemMapper::toDomain);
    }

    @Override
    public MenuItem save(MenuItem menuItem) {
        return menuItemMapper.toDomain(jpaMenuItemRepository.save(menuItemMapper.toEntity(menuItem)));
    }

    @Override
    public long count() {
        return jpaMenuItemRepository.count();
    }
}
