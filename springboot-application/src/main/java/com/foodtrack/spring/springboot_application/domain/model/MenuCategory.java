package com.foodtrack.spring.springboot_application.domain.model;

public enum MenuCategory {
    APPETIZER(1),
    SOUP(2),
    MAIN_COURSE(3),
    SALAD(4),
    DESSERT(5),
    DRINK(6);

    private final int displayOrder;

    MenuCategory(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }
}
