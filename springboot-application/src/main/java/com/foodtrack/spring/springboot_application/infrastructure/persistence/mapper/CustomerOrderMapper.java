package com.foodtrack.spring.springboot_application.infrastructure.persistence.mapper;

import com.foodtrack.spring.springboot_application.domain.model.CustomerOrder;
import com.foodtrack.spring.springboot_application.domain.model.OrderLine;
import com.foodtrack.spring.springboot_application.domain.model.OrderStatus;
import com.foodtrack.spring.springboot_application.infrastructure.persistence.entity.CustomerOrderEntity;
import com.foodtrack.spring.springboot_application.infrastructure.persistence.entity.OrderLineEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CustomerOrderMapper {

    public CustomerOrder toDomain(CustomerOrderEntity entity) {
        List<OrderLine> lines = entity.getLines().stream()
                .map(line -> new OrderLine(
                        line.getId(),
                        line.getMenuItemId(),
                        line.getItemName(),
                        line.getQuantity(),
                        line.getUnitPrice(),
                        line.getSubtotal()
                ))
                .toList();

        return new CustomerOrder(
                entity.getId(),
                entity.getTableId(),
                entity.getCreatedByUserId(),
                OrderStatus.valueOf(entity.getStatus()),
                entity.getTotal(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                lines
        );
    }

    public CustomerOrderEntity toEntity(CustomerOrder order) {
        CustomerOrderEntity entity = new CustomerOrderEntity();
        entity.setId(order.id());
        entity.setTableId(order.tableId());
        entity.setCreatedByUserId(order.createdByUserId());
        entity.setStatus(order.status().name());
        entity.setTotal(order.total());
        entity.setCreatedAt(order.createdAt());
        entity.setUpdatedAt(order.updatedAt());

        List<OrderLineEntity> lineEntities = new ArrayList<>();
        for (OrderLine line : order.lines()) {
            OrderLineEntity lineEntity = new OrderLineEntity();
            lineEntity.setId(line.id());
            lineEntity.setMenuItemId(line.menuItemId());
            lineEntity.setItemName(line.itemName());
            lineEntity.setQuantity(line.quantity());
            lineEntity.setUnitPrice(line.unitPrice());
            lineEntity.setSubtotal(line.subtotal());
            lineEntity.setOrder(entity);
            lineEntities.add(lineEntity);
        }
        entity.setLines(lineEntities);
        return entity;
    }
}
