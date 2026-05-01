package com.foodtrack.spring.springboot_application.application.service;

import com.foodtrack.spring.springboot_application.application.port.in.MenuUseCase;
import com.foodtrack.spring.springboot_application.application.port.out.MenuItemRepositoryPort;
import com.foodtrack.spring.springboot_application.domain.model.MenuItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

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
                .sorted(Comparator
                        .comparingInt((MenuItem item) -> item.category().getDisplayOrder())
                        .thenComparing(MenuItem::name))
                .toList();
        logger.debug("Returning {} menu items", items.size());
        return items;
    }
}