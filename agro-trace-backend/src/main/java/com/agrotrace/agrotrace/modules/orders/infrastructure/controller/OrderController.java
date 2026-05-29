package com.agrotrace.agrotrace.modules.orders.infrastructure.controller;

import com.agrotrace.agrotrace.modules.orders.application.service.OrderService;
import com.agrotrace.agrotrace.modules.orders.domain.model.Order;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Tag(name = "Orders", description = "Gestion de pedidos")
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/orders")
    @Operation(summary = "Mis pedidos")
    public ResponseEntity<List<Order>> getMyOrders(@RequestParam UUID userId) {
        return ResponseEntity.ok(orderService.getMyOrders(userId));
    }

    @GetMapping("/orders/selling")
    @Operation(summary = "Pedidos recibidos (productor)")
    public ResponseEntity<List<Order>> getSellingOrders() {
        return ResponseEntity.ok(orderService.getSellingOrders());
    }

    @GetMapping("/orders/{id}")
    @Operation(summary = "Detalle de pedido con historial de estados")
    public ResponseEntity<Order> getOrder(@PathVariable UUID id) {
        return ResponseEntity.ok(orderService.getOrder(id));
    }

    @PatchMapping("/orders/{id}/status")
    @Operation(summary = "Cambiar estado del pedido")
    public ResponseEntity<Order> updateStatus(
            @PathVariable UUID id,
            @RequestParam String status,
            @RequestParam UUID changedBy,
            @RequestParam(required = false) String notes) {
        return ResponseEntity.ok(orderService.updateStatus(id, status, changedBy, notes));
    }

    @PostMapping("/orders/{id}/cancel")
    @Operation(summary = "Cancelar pedido")
    public ResponseEntity<Order> cancelOrder(
            @PathVariable UUID id,
            @RequestParam UUID userId,
            @RequestParam(required = false) String reason) {
        return ResponseEntity.ok(orderService.cancelOrder(id, userId, reason));
    }
}
