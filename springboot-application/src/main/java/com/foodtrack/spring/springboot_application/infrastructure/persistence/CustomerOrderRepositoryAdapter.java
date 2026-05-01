package com.foodtrack.spring.springboot_application.infrastructure.persistence;

import com.foodtrack.spring.springboot_application.application.port.out.CustomerOrderRepositoryPort;
import com.foodtrack.spring.springboot_application.domain.model.CustomerOrder;
import com.foodtrack.spring.springboot_application.domain.model.OrderStatus;
import com.foodtrack.spring.springboot_application.infrastructure.persistence.mapper.CustomerOrderMapper;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class CustomerOrderRepositoryAdapter implements CustomerOrderRepositoryPort {

    private final JpaCustomerOrderRepository jpaCustomerOrderRepository;
    private final CustomerOrderMapper customerOrderMapper;

    public CustomerOrderRepositoryAdapter(
            JpaCustomerOrderRepository jpaCustomerOrderRepository,
            CustomerOrderMapper customerOrderMapper
    ) {
        this.jpaCustomerOrderRepository = jpaCustomerOrderRepository;
        this.customerOrderMapper = customerOrderMapper;
    }

    @Override
    public Optional<CustomerOrder> findOpenByTableId(Long tableId) {
        return jpaCustomerOrderRepository.findFirstByTableIdAndStatusOrderByCreatedAtDesc(tableId, "OPEN")
                .map(customerOrderMapper::toDomain);
    }

    @Override
    public Map<Long, CustomerOrder> findOpenByTableIds(Collection<Long> tableIds) {
        Map<Long, CustomerOrder> openOrders = new LinkedHashMap<>();
        if (tableIds.isEmpty()) {
            return openOrders;
        }

        List<CustomerOrder> orders = jpaCustomerOrderRepository
                .findByTableIdInAndStatusOrderByUpdatedAtDesc(tableIds, "OPEN")
                .stream()
                .map(customerOrderMapper::toDomain)
                .toList();

        for (CustomerOrder order : orders) {
            openOrders.putIfAbsent(order.tableId(), order);
        }
        return openOrders;
    }

    @Override
    @SuppressWarnings("null")
    public CustomerOrder save(CustomerOrder order) {
        return customerOrderMapper.toDomain(jpaCustomerOrderRepository.save(customerOrderMapper.toEntity(order)));
    }

    @Override
    @SuppressWarnings("null")
    public void deleteById(Long id) {
        jpaCustomerOrderRepository.deleteById(id);
    }

    @Override
    @SuppressWarnings("null")
    public Optional<CustomerOrder> findById(Long id) {
        return jpaCustomerOrderRepository.findById(id).map(customerOrderMapper::toDomain);
    }

    @Override
    public List<CustomerOrder> findByTableId(Long tableId) {
        return jpaCustomerOrderRepository.findByTableIdOrderByCreatedAtDesc(tableId).stream()
                .map(customerOrderMapper::toDomain)
                .toList();
    }

    @Override
    public List<CustomerOrder> findByStatuses(List<OrderStatus> statuses) {
        return jpaCustomerOrderRepository.findByStatusInOrderByUpdatedAtDesc(
                        statuses.stream().map(OrderStatus::name).toList()
                ).stream()
                .map(customerOrderMapper::toDomain)
                .toList();
    }
}
