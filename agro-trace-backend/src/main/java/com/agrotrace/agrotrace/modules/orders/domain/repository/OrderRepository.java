package com.agrotrace.agrotrace.modules.orders.domain.repository;

import com.agrotrace.agrotrace.modules.orders.domain.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findByUserIdOrderByCreatedAtDesc(UUID userId);
}
