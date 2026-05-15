package com.foodtrack.spring.springboot_application.application.port.in;

import com.foodtrack.spring.springboot_application.domain.model.MenuItem;

import java.math.BigDecimal;
import java.util.List;

public interface MenuUseCase {
    List<MenuItem> listMenuItems();
    List<MenuItem> listAllMenuItems();
    MenuItem createMenuItem(String name, String description, String category, BigDecimal price, int stockQuantity);
    MenuItem updateMenuItem(Long id, String name, String description, String category, BigDecimal price, int stockQuantity, boolean active);
    void deleteMenuItem(Long id);
}
