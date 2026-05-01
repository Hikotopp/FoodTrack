package com.foodtrack.spring.springboot_application.application.port.out;

import com.foodtrack.spring.springboot_application.domain.model.RestaurantTable;

import java.util.List;
import java.util.Optional;

import org.springframework.lang.NonNull;

public interface RestaurantTableRepositoryPort {
    List<RestaurantTable> findAll();
    Optional<RestaurantTable> findById(Long id);
    Optional<RestaurantTable> findByTableNumber(int tableNumber);
    RestaurantTable save(RestaurantTable table);
    void deleteById(@NonNull Long id);
    long count();
}
