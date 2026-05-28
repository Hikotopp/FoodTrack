package com.foodtrack.spring.springboot_application.application.service;

import com.foodtrack.spring.springboot_application.application.model.SalesHistoryView;
import com.foodtrack.spring.springboot_application.application.port.out.CustomerOrderRepositoryPort;
import com.foodtrack.spring.springboot_application.application.port.out.RestaurantTableRepositoryPort;
import com.foodtrack.spring.springboot_application.application.port.out.UserRepositoryPort;
import com.foodtrack.spring.springboot_application.domain.exception.BusinessRuleException;
import com.foodtrack.spring.springboot_application.domain.exception.ResourceNotFoundException;
import com.foodtrack.spring.springboot_application.domain.model.AppUser;
import com.foodtrack.spring.springboot_application.domain.model.CustomerOrder;
import com.foodtrack.spring.springboot_application.domain.model.OrderStatus;
import com.foodtrack.spring.springboot_application.domain.model.RestaurantTable;
import com.foodtrack.spring.springboot_application.domain.model.TableStatus;
import com.foodtrack.spring.springboot_application.domain.model.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("SalesHistoryApplicationService Unit Tests")
class SalesHistoryApplicationServiceTest {

    @Mock
    private CustomerOrderRepositoryPort customerOrderRepositoryPort;

    @Mock
    private RestaurantTableRepositoryPort restaurantTableRepositoryPort;

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @InjectMocks
    private SalesHistoryApplicationService service;

    @Test
    void listHistoryCombinesOrdersWithTableAndUserData() {
        CustomerOrder order = order(10L, 3L, 2L, OrderStatus.CLOSED);
        when(restaurantTableRepositoryPort.findAll()).thenReturn(List.of(new RestaurantTable(3L, 8, TableStatus.AVAILABLE)));
        when(userRepositoryPort.findAll()).thenReturn(List.of(new AppUser(2L, "Cashier", "cashier@example.com", "hash", UserRole.EMPLOYEE)));
        when(customerOrderRepositoryPort.findByStatuses(List.of(OrderStatus.CLOSED, OrderStatus.CANCELLED))).thenReturn(List.of(order));

        List<SalesHistoryView> result = service.listHistory();

        assertEquals(1, result.size());
        assertEquals(8, result.get(0).tableNumber());
        assertEquals("Cashier", result.get(0).createdByName());
        assertEquals(OrderStatus.CLOSED, result.get(0).status());
    }

    @Test
    void listHistoryUsesFallbackWhenUserWasDeleted() {
        when(restaurantTableRepositoryPort.findAll()).thenReturn(List.of());
        when(userRepositoryPort.findAll()).thenReturn(List.of());
        when(customerOrderRepositoryPort.findByStatuses(List.of(OrderStatus.CLOSED, OrderStatus.CANCELLED)))
                .thenReturn(List.of(order(11L, 99L, 88L, OrderStatus.CANCELLED)));

        SalesHistoryView result = service.listHistory().get(0);

        assertEquals(null, result.tableNumber());
        assertEquals("Usuario eliminado", result.createdByName());
    }

    @Test
    void updateSaleStatusRejectsOpenStatusAndOpenOrders() {
        assertThrows(BusinessRuleException.class, () -> service.updateSaleStatus(10L, OrderStatus.OPEN));

        when(customerOrderRepositoryPort.findById(10L)).thenReturn(Optional.of(order(10L, 1L, 1L, OrderStatus.OPEN)));
        assertThrows(BusinessRuleException.class, () -> service.updateSaleStatus(10L, OrderStatus.CANCELLED));
    }

    @Test
    void updateSaleStatusPersistsHistoricalStatus() {
        CustomerOrder existing = order(10L, 1L, 2L, OrderStatus.CLOSED);
        when(customerOrderRepositoryPort.findById(10L)).thenReturn(Optional.of(existing));
        when(customerOrderRepositoryPort.save(any(CustomerOrder.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(restaurantTableRepositoryPort.findById(1L)).thenReturn(Optional.of(new RestaurantTable(1L, 5, TableStatus.AVAILABLE)));
        when(userRepositoryPort.findById(2L)).thenReturn(Optional.of(new AppUser(2L, "Cashier", "cashier@example.com", "hash", UserRole.EMPLOYEE)));

        SalesHistoryView result = service.updateSaleStatus(10L, OrderStatus.CANCELLED);

        assertEquals(OrderStatus.CANCELLED, result.status());
        assertEquals(5, result.tableNumber());
        assertEquals("Cashier", result.createdByName());
    }

    @Test
    void updateSaleStatusThrowsWhenSaleDoesNotExist() {
        when(customerOrderRepositoryPort.findById(404L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.updateSaleStatus(404L, OrderStatus.CLOSED));
    }

    private CustomerOrder order(Long id, Long tableId, Long userId, OrderStatus status) {
        LocalDateTime now = LocalDateTime.of(2026, 5, 28, 12, 0);
        return new CustomerOrder(id, tableId, userId, status, BigDecimal.valueOf(42), now, now, List.of());
    }
}
