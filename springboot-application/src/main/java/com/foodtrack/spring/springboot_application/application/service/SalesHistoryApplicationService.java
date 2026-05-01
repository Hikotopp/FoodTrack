package com.foodtrack.spring.springboot_application.application.service;

import com.foodtrack.spring.springboot_application.application.model.SalesHistoryView;
import com.foodtrack.spring.springboot_application.application.port.in.SalesHistoryUseCase;
import com.foodtrack.spring.springboot_application.application.port.out.CustomerOrderRepositoryPort;
import com.foodtrack.spring.springboot_application.application.port.out.RestaurantTableRepositoryPort;
import com.foodtrack.spring.springboot_application.application.port.out.UserRepositoryPort;
import com.foodtrack.spring.springboot_application.domain.exception.BusinessRuleException;
import com.foodtrack.spring.springboot_application.domain.exception.ResourceNotFoundException;
import com.foodtrack.spring.springboot_application.domain.model.AppUser;
import com.foodtrack.spring.springboot_application.domain.model.CustomerOrder;
import com.foodtrack.spring.springboot_application.domain.model.OrderStatus;
import com.foodtrack.spring.springboot_application.domain.model.RestaurantTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class SalesHistoryApplicationService implements SalesHistoryUseCase {

    private final CustomerOrderRepositoryPort customerOrderRepositoryPort;
    private final RestaurantTableRepositoryPort restaurantTableRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;

    public SalesHistoryApplicationService(
            CustomerOrderRepositoryPort customerOrderRepositoryPort,
            RestaurantTableRepositoryPort restaurantTableRepositoryPort,
            UserRepositoryPort userRepositoryPort
    ) {
        this.customerOrderRepositoryPort = customerOrderRepositoryPort;
        this.restaurantTableRepositoryPort = restaurantTableRepositoryPort;
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalesHistoryView> listHistory() {
        Map<Long, RestaurantTable> tablesById = restaurantTableRepositoryPort.findAll().stream()
                .collect(Collectors.toMap(RestaurantTable::id, Function.identity()));
        Map<Long, AppUser> usersById = userRepositoryPort.findAll().stream()
                .collect(Collectors.toMap(AppUser::id, Function.identity()));

        return customerOrderRepositoryPort.findByStatuses(List.of(OrderStatus.CLOSED, OrderStatus.CANCELLED)).stream()
                .map(order -> toView(order, tablesById.get(order.tableId()), usersById.get(order.createdByUserId())))
                .toList();
    }

    @Override
    public SalesHistoryView updateSaleStatus(Long orderId, OrderStatus status) {
        if (status == OrderStatus.OPEN) {
            throw new BusinessRuleException("Historical sales can only be CLOSED or CANCELLED.");
        }

        CustomerOrder order = customerOrderRepositoryPort.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Sale was not found."));

        if (order.status() == OrderStatus.OPEN) {
            throw new BusinessRuleException("Open orders must be managed from the table dashboard.");
        }

        CustomerOrder updated = customerOrderRepositoryPort.save(new CustomerOrder(
                order.id(),
                order.tableId(),
                order.createdByUserId(),
                status,
                order.total(),
                order.createdAt(),
                LocalDateTime.now(),
                order.lines()
        ));

        RestaurantTable table = restaurantTableRepositoryPort.findById(updated.tableId()).orElse(null);
        AppUser user = userRepositoryPort.findById(updated.createdByUserId()).orElse(null);
        return toView(updated, table, user);
    }

    private SalesHistoryView toView(CustomerOrder order, RestaurantTable table, AppUser user) {
        return new SalesHistoryView(
                order.id(),
                order.tableId(),
                table == null ? null : table.tableNumber(),
                order.createdByUserId(),
                user == null ? "Usuario eliminado" : user.fullName(),
                order.status(),
                order.total(),
                order.createdAt(),
                order.updatedAt(),
                order.lines()
        );
    }
}
