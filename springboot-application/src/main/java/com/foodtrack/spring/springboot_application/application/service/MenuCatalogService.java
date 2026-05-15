package com.foodtrack.spring.springboot_application.application.service;

import com.foodtrack.spring.springboot_application.application.port.in.MenuUseCase;
import com.foodtrack.spring.springboot_application.application.port.out.MenuItemRepositoryPort;
import com.foodtrack.spring.springboot_application.domain.exception.BusinessRuleException;
import com.foodtrack.spring.springboot_application.domain.exception.ResourceNotFoundException;
import com.foodtrack.spring.springboot_application.domain.model.MenuCategory;
import com.foodtrack.spring.springboot_application.domain.model.MenuItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Service
@Transactional(readOnly = true)
public class MenuCatalogService implements MenuUseCase {

    private static final Logger logger = LoggerFactory.getLogger(MenuCatalogService.class);

    private final MenuItemRepositoryPort menuItemRepositoryPort;

    public MenuCatalogService(MenuItemRepositoryPort menuItemRepositoryPort) {
        this.menuItemRepositoryPort = menuItemRepositoryPort;
    }

    @Override
    public List<MenuItem> listMenuItems() {
        logger.debug("Fetching all active menu items");
        List<MenuItem> items = menuItemRepositoryPort.findAllActive().stream()
                .filter(item -> item.stockQuantity() > 0)
                .sorted(Comparator
                        .comparingInt((MenuItem item) -> item.category().getDisplayOrder())
                        .thenComparing(MenuItem::name))
                .toList();
        logger.debug("Returning {} menu items", items.size());
        return items;
    }

    @Override
    public List<MenuItem> listAllMenuItems() {
        return sortMenuItems(menuItemRepositoryPort.findAll());
    }

    @Override
    @Transactional
    public MenuItem createMenuItem(String name, String description, String category, BigDecimal price, int stockQuantity) {
        MenuCategory menuCategory = parseCategory(category);
        validateMenuItem(name, description, price, stockQuantity);
        return menuItemRepositoryPort.save(new MenuItem(
                null,
                name.trim(),
                description.trim(),
                menuCategory,
                price,
                stockQuantity,
                true
        ));
    }

    @Override
    @Transactional
    public MenuItem updateMenuItem(Long id, String name, String description, String category, BigDecimal price, int stockQuantity, boolean active) {
        MenuItem existing = getMenuItem(id);
        MenuCategory menuCategory = parseCategory(category);
        validateMenuItem(name, description, price, stockQuantity);
        return menuItemRepositoryPort.save(new MenuItem(
                existing.id(),
                name.trim(),
                description.trim(),
                menuCategory,
                price,
                stockQuantity,
                active
        ));
    }

    @Override
    @Transactional
    public void deleteMenuItem(Long id) {
        MenuItem existing = getMenuItem(id);
        menuItemRepositoryPort.save(new MenuItem(
                existing.id(),
                existing.name(),
                existing.description(),
                existing.category(),
                existing.price(),
                existing.stockQuantity(),
                false
        ));
    }

    private MenuItem getMenuItem(Long id) {
        return menuItemRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found."));
    }

    private List<MenuItem> sortMenuItems(List<MenuItem> items) {
        return items.stream()
                .sorted(Comparator
                        .comparingInt((MenuItem item) -> item.category().getDisplayOrder())
                        .thenComparing(MenuItem::name))
                .toList();
    }

    private MenuCategory parseCategory(String category) {
        try {
            return MenuCategory.valueOf(category.trim().toUpperCase(Locale.ROOT));
        } catch (RuntimeException ex) {
            throw new BusinessRuleException("Invalid menu category.");
        }
    }

    private void validateMenuItem(String name, String description, BigDecimal price, int stockQuantity) {
        if (name == null || name.trim().length() < 2) {
            throw new BusinessRuleException("Menu item name must have at least 2 characters.");
        }
        if (description == null || description.trim().length() < 2) {
            throw new BusinessRuleException("Menu item description must have at least 2 characters.");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessRuleException("Menu item price must be greater than zero.");
        }
        if (stockQuantity < 0) {
            throw new BusinessRuleException("Stock quantity cannot be negative.");
        }
    }
}
