package com.agrotrace.agrotrace.modules.orders.application.service;

import com.agrotrace.agrotrace.modules.orders.domain.model.Order;
import com.agrotrace.agrotrace.modules.orders.domain.model.OrderStatusHistory;
import com.agrotrace.agrotrace.modules.orders.domain.repository.OrderRepository;
import com.agrotrace.agrotrace.modules.orders.domain.repository.OrderStatusHistoryRepository;
import com.agrotrace.agrotrace.shared.exceptions.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderStatusHistoryRepository statusHistoryRepository;

    public List<Order> getMyOrders(UUID userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public List<Order> getSellingOrders() {
        return orderRepository.findAll();
    }

    public Order getOrder(UUID id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new BusinessException("ORDER_NOT_FOUND", "Pedido no encontrado", 404));
    }

    @Transactional
    public Order updateStatus(UUID orderId, String newStatus, UUID changedBy, String notes) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException("ORDER_NOT_FOUND", "Pedido no encontrado", 404));

        if ("DELIVERED".equals(order.getStatus()) || "CANCELLED".equals(order.getStatus())) {
            throw new BusinessException("ORDER_FINAL", "El pedido ya esta en estado final");
        }

        order.setStatus(newStatus);

        OrderStatusHistory history = OrderStatusHistory.builder()
                .order(order)
                .status(newStatus)
                .changedBy(changedBy)
                .notes(notes)
                .build();
        order.getStatusHistory().add(history);

        return orderRepository.save(order);
    }

    @Transactional
    public Order cancelOrder(UUID orderId, UUID userId, String reason) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException("ORDER_NOT_FOUND", "Pedido no encontrado", 404));

        if (!"PENDING".equals(order.getStatus())) {
            throw new BusinessException("CANCEL_NOT_ALLOWED", "Solo se puede cancelar pedidos PENDING");
        }

        order.setStatus("CANCELLED");

        OrderStatusHistory history = OrderStatusHistory.builder()
                .order(order)
                .status("CANCELLED")
                .changedBy(userId)
                .notes(reason)
                .build();
        order.getStatusHistory().add(history);

        return orderRepository.save(order);
    }
}
