package com.foodtrack.spring.springboot_application.infrastructure.persistence;

import com.foodtrack.spring.springboot_application.application.port.out.RestaurantTableRepositoryPort;
import com.foodtrack.spring.springboot_application.domain.model.RestaurantTable;
import com.foodtrack.spring.springboot_application.infrastructure.persistence.mapper.RestaurantTableMapper;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class RestaurantTableRepositoryAdapter implements RestaurantTableRepositoryPort {

    private final JpaRestaurantTableRepository jpaRestaurantTableRepository;
    private final RestaurantTableMapper restaurantTableMapper;

    public RestaurantTableRepositoryAdapter(
            JpaRestaurantTableRepository jpaRestaurantTableRepository,
            RestaurantTableMapper restaurantTableMapper
    ) {
        this.jpaRestaurantTableRepository = jpaRestaurantTableRepository;
        this.restaurantTableMapper = restaurantTableMapper;
    }

    @Override
    public List<RestaurantTable> findAll() {
        return jpaRestaurantTableRepository.findAll().stream()
                .map(restaurantTableMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<RestaurantTable> findById(Long id) {
        return jpaRestaurantTableRepository.findById(id).map(restaurantTableMapper::toDomain);
    }

    @Override
    public Optional<RestaurantTable> findByTableNumber(int tableNumber) {
        return jpaRestaurantTableRepository.findByTableNumber(tableNumber).map(restaurantTableMapper::toDomain);
    }

    @Override
    public RestaurantTable save(RestaurantTable table) {
        return restaurantTableMapper.toDomain(jpaRestaurantTableRepository.save(restaurantTableMapper.toEntity(table)));
    }

    @Override
    public void deleteById(@NonNull Long id) {
        jpaRestaurantTableRepository.deleteById(id);
    }

    @Override
    public long count() {
        return jpaRestaurantTableRepository.count();
    }
}
