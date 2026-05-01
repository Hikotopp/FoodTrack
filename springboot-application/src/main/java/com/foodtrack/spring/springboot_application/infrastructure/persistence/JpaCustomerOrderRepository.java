package com.foodtrack.spring.springboot_application.infrastructure.persistence;

import com.foodtrack.spring.springboot_application.infrastructure.persistence.entity.CustomerOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface JpaCustomerOrderRepository extends JpaRepository<CustomerOrderEntity, Long> {
    Optional<CustomerOrderEntity> findFirstByTableIdAndStatusOrderByCreatedAtDesc(Long tableId, String status);
    List<CustomerOrderEntity> findByTableIdInAndStatusOrderByUpdatedAtDesc(Collection<Long> tableIds, String status);
    List<CustomerOrderEntity> findByTableIdOrderByCreatedAtDesc(Long tableId);
}
