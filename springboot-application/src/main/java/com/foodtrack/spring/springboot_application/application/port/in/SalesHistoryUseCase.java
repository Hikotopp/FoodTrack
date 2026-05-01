package com.foodtrack.spring.springboot_application.application.port.in;

import com.foodtrack.spring.springboot_application.application.model.SalesHistoryView;
import com.foodtrack.spring.springboot_application.domain.model.OrderStatus;

import java.util.List;

public interface SalesHistoryUseCase {
    List<SalesHistoryView> listHistory();
    SalesHistoryView updateSaleStatus(Long orderId, OrderStatus status);
}
