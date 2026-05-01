package com.foodtrack.spring.springboot_application.infrastructure.persistence;

import com.foodtrack.spring.springboot_application.infrastructure.persistence.entity.RestaurantTableEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaRestaurantTableRepository extends JpaRepository<RestaurantTableEntity, Long> {
    Optional<RestaurantTableEntity> findByTableNumber(Integer tableNumber);
}
