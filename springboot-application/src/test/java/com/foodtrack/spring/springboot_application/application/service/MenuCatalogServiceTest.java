package com.foodtrack.spring.springboot_application.application.service;

import com.foodtrack.spring.springboot_application.application.port.out.MenuItemRepositoryPort;
import com.foodtrack.spring.springboot_application.domain.exception.BusinessRuleException;
import com.foodtrack.spring.springboot_application.domain.exception.ResourceNotFoundException;
import com.foodtrack.spring.springboot_application.domain.model.MenuCategory;
import com.foodtrack.spring.springboot_application.domain.model.MenuItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("MenuCatalogService Unit Tests")
class MenuCatalogServiceTest {

    @Mock
    private MenuItemRepositoryPort menuItemRepositoryPort;

    @InjectMocks
    private MenuCatalogService service;

    @Test
    void listMenuItemsReturnsOnlyActiveItemsWithStockSortedByCategoryAndName() {
        MenuItem dessert = item(1L, "Cake", MenuCategory.DESSERT, 3, true);
        MenuItem burgerB = item(2L, "B Burger", MenuCategory.BURGER, 4, true);
        MenuItem burgerA = item(3L, "A Burger", MenuCategory.BURGER, 2, true);
        MenuItem outOfStock = item(4L, "Empty", MenuCategory.APPETIZER, 0, true);
        when(menuItemRepositoryPort.findAllActive()).thenReturn(List.of(dessert, burgerB, outOfStock, burgerA));

        List<MenuItem> result = service.listMenuItems();

        assertEquals(List.of(burgerA, burgerB, dessert), result);
    }

    @Test
    void createMenuItemTrimsValuesParsesCategoryAndPersistsActiveItem() {
        MenuItem saved = item(10L, "Fries", MenuCategory.APPETIZER, 8, true);
        when(menuItemRepositoryPort.save(any(MenuItem.class))).thenReturn(saved);

        MenuItem result = service.createMenuItem(" Fries ", " Crispy ", " appetizer ", BigDecimal.valueOf(7), 8);

        assertEquals(saved, result);
        verify(menuItemRepositoryPort).save(new MenuItem(null, "Fries", "Crispy", MenuCategory.APPETIZER, BigDecimal.valueOf(7), 8, true));
    }

    @Test
    void createMenuItemRejectsInvalidInput() {
        assertThrows(BusinessRuleException.class,
                () -> service.createMenuItem("A", "Good", "BURGER", BigDecimal.TEN, 1));
        assertThrows(BusinessRuleException.class,
                () -> service.createMenuItem("Burger", "Good", "UNKNOWN", BigDecimal.TEN, 1));
        assertThrows(BusinessRuleException.class,
                () -> service.createMenuItem("Burger", "Good", "BURGER", BigDecimal.ZERO, 1));
        assertThrows(BusinessRuleException.class,
                () -> service.createMenuItem("Burger", "Good", "BURGER", BigDecimal.TEN, -1));
    }

    @Test
    void updateMenuItemKeepsExistingIdAndAllowsInactiveItems() {
        when(menuItemRepositoryPort.findById(5L)).thenReturn(Optional.of(item(5L, "Old", MenuCategory.OTHER, 1, true)));
        when(menuItemRepositoryPort.save(any(MenuItem.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MenuItem result = service.updateMenuItem(5L, "Tea", "Green tea", "DRINK", BigDecimal.valueOf(2), 12, false);

        assertEquals(new MenuItem(5L, "Tea", "Green tea", MenuCategory.DRINK, BigDecimal.valueOf(2), 12, false), result);
    }

    @Test
    void deleteMenuItemSoftDeletesExistingItem() {
        MenuItem existing = item(7L, "Soup", MenuCategory.SOUP, 9, true);
        when(menuItemRepositoryPort.findById(7L)).thenReturn(Optional.of(existing));

        service.deleteMenuItem(7L);

        verify(menuItemRepositoryPort).save(new MenuItem(7L, "Soup", "Description", MenuCategory.SOUP, BigDecimal.valueOf(10), 9, false));
    }

    @Test
    void updateMenuItemThrowsWhenItemDoesNotExist() {
        when(menuItemRepositoryPort.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.updateMenuItem(99L, "Tea", "Green tea", "DRINK", BigDecimal.ONE, 1, true));
    }

    private MenuItem item(Long id, String name, MenuCategory category, int stock, boolean active) {
        return new MenuItem(id, name, "Description", category, BigDecimal.valueOf(10), stock, active);
    }
}
