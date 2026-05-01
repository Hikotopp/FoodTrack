package com.foodtrack.spring.springboot_application.application.port.in;

import com.foodtrack.spring.springboot_application.domain.model.MenuItem;

import java.util.List;

public interface MenuUseCase {
    List<MenuItem> listMenuItems();
}
