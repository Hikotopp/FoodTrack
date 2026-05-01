package com.foodtrack.spring.springboot_application.infrastructure.persistence.mapper;

import com.foodtrack.spring.springboot_application.domain.model.RestaurantTable;
import com.foodtrack.spring.springboot_application.domain.model.TableStatus;
import com.foodtrack.spring.springboot_application.infrastructure.persistence.entity.RestaurantTableEntity;
import org.springframework.stereotype.Component;

@Component
public class RestaurantTableMapper {

    public RestaurantTable toDomain(RestaurantTableEntity entity) {
        return new RestaurantTable(
                entity.getId(),
                entity.getTableNumber(),
                TableStatus.valueOf(entity.getStatus())
        );
    }

    public RestaurantTableEntity toEntity(RestaurantTable table) {
        RestaurantTableEntity entity = new RestaurantTableEntity();
        entity.setId(table.id());
        entity.setTableNumber(table.tableNumber());
        entity.setStatus(table.status().name());
        return entity;
    }
}
