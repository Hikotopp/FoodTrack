package com.foodtrack.spring.springboot_application.entrypoint.menu;

import com.foodtrack.spring.springboot_application.domain.model.MenuItem;
import org.springframework.stereotype.Component;

@Component
public class MenuItemRestMapper {

    public MenuItemResponse toResponse(MenuItem item) {
        return new MenuItemResponse(
                item.id(),
                item.name(),
                item.description(),
                item.category().name(),
                item.price(),
                item.active()
        );
    }
}