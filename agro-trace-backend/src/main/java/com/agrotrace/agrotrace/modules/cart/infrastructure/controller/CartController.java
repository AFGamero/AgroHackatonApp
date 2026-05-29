package com.agrotrace.agrotrace.modules.cart.infrastructure.controller;

import com.agrotrace.agrotrace.modules.cart.application.service.CartService;
import com.agrotrace.agrotrace.modules.cart.domain.model.Cart;
import com.agrotrace.agrotrace.modules.orders.domain.model.Order;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@Tag(name = "Cart", description = "Carrito de compras")
public class CartController {

    private final CartService cartService;

    @GetMapping
    @Operation(summary = "Ver mi carrito activo")
    public ResponseEntity<Cart> getCart(@RequestParam UUID userId) {
        return ResponseEntity.ok(cartService.getCart(userId));
    }

    @PostMapping("/items")
    @Operation(summary = "Agregar producto al carrito")
    public ResponseEntity<Cart> addItem(
            @RequestParam UUID userId,
            @RequestParam UUID productId,
            @RequestParam BigDecimal quantity) {
        return ResponseEntity.ok(cartService.addItem(userId, productId, quantity));
    }

    @PutMapping("/items/{itemId}")
    @Operation(summary = "Modificar cantidad de un item")
    public ResponseEntity<Cart> updateItem(
            @RequestParam UUID userId,
            @PathVariable UUID itemId,
            @RequestParam BigDecimal quantity) {
        return ResponseEntity.ok(cartService.updateItemQuantity(userId, itemId, quantity));
    }

    @DeleteMapping("/items/{itemId}")
    @Operation(summary = "Quitar producto del carrito")
    public ResponseEntity<Cart> removeItem(@RequestParam UUID userId, @PathVariable UUID itemId) {
        return ResponseEntity.ok(cartService.removeItem(userId, itemId));
    }

    @DeleteMapping
    @Operation(summary = "Vaciar carrito")
    public ResponseEntity<Void> clearCart(@RequestParam UUID userId) {
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/checkout")
    @Operation(summary = "Convertir carrito en pedido")
    public ResponseEntity<Order> checkout(
            @RequestParam UUID userId,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String address) {
        return ResponseEntity.ok(cartService.checkout(userId, country, email, phone, address));
    }
}
