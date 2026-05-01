package com.foodtrack.spring.springboot_application.application.port.out;

import com.foodtrack.spring.springboot_application.domain.model.CustomerOrder;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CustomerOrderRepositoryPort {
    Optional<CustomerOrder> findOpenByTableId(Long tableId);
    Map<Long, CustomerOrder> findOpenByTableIds(Collection<Long> tableIds);
    CustomerOrder save(CustomerOrder order);
    void deleteById(Long id);
    Optional<CustomerOrder> findById(Long id);
    List<CustomerOrder> findByTableId(Long tableId);
}
