package com.foodtrack.spring.springboot_application.domain.model;

public enum MenuCategory {
    APPETIZER(1),
    BURGER(2),
    HOT_DOG(3),
    OTHER(4),
    DRINK(5),
    DESSERT(6),
    COMBO(7),
    ADDITIONAL(8),
    PROMOTION(9),
    SOUP(90),
    MAIN_COURSE(91),
    SALAD(92);

    private final int displayOrder;

    MenuCategory(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }
}
