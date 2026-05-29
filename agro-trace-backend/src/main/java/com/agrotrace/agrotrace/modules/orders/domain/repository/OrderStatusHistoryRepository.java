package com.agrotrace.agrotrace.modules.orders.domain.repository;

import com.agrotrace.agrotrace.modules.orders.domain.model.OrderStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface OrderStatusHistoryRepository extends JpaRepository<OrderStatusHistory, UUID> {
}
