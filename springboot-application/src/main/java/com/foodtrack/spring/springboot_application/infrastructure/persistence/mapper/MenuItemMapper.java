package com.foodtrack.spring.springboot_application.infrastructure.persistence.mapper;

import com.foodtrack.spring.springboot_application.domain.model.MenuCategory;
import com.foodtrack.spring.springboot_application.domain.model.MenuItem;
import com.foodtrack.spring.springboot_application.infrastructure.persistence.entity.MenuItemEntity;
import org.springframework.stereotype.Component;

@Component
public class MenuItemMapper {

    public MenuItem toDomain(MenuItemEntity entity) {
        return new MenuItem(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                MenuCategory.valueOf(entity.getCategory()),
                entity.getPrice(),
                entity.isActive()
        );
    }

    public MenuItemEntity toEntity(MenuItem menuItem) {
        MenuItemEntity entity = new MenuItemEntity();
        entity.setId(menuItem.id());
        entity.setName(menuItem.name());
        entity.setDescription(menuItem.description());
        entity.setCategory(menuItem.category().name());
        entity.setPrice(menuItem.price());
        entity.setActive(menuItem.active());
        return entity;
    }
}
