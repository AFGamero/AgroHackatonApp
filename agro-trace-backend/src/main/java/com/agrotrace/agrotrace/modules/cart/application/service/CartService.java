package com.agrotrace.agrotrace.modules.cart.application.service;

import com.agrotrace.agrotrace.modules.cart.domain.model.Cart;
import com.agrotrace.agrotrace.modules.cart.domain.model.CartItem;
import com.agrotrace.agrotrace.modules.cart.domain.repository.CartRepository;
import com.agrotrace.agrotrace.modules.orders.domain.model.Order;
import com.agrotrace.agrotrace.modules.orders.domain.model.OrderItem;
import com.agrotrace.agrotrace.modules.orders.domain.model.OrderStatusHistory;
import com.agrotrace.agrotrace.modules.orders.domain.repository.OrderRepository;
import com.agrotrace.agrotrace.modules.products.domain.model.Product;
import com.agrotrace.agrotrace.modules.products.domain.repository.ProductRepository;
import com.agrotrace.agrotrace.shared.exceptions.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public Cart getCart(UUID userId) {
        return cartRepository.findByUserIdAndStatus(userId, "ACTIVE")
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUserId(userId);
                    cart.setStatus("ACTIVE");
                    return cartRepository.save(cart);
                });
    }

    @Transactional
    public Cart addItem(UUID userId, UUID productId, BigDecimal quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException("PRODUCT_NOT_FOUND", "Producto no encontrado", 404));

        if (!"PUBLICADO".equals(product.getStatus())) {
            throw new BusinessException("PRODUCT_NOT_AVAILABLE", "Producto no disponible");
        }

        if (quantity.compareTo(product.getQuantityAvailable()) > 0) {
            throw new BusinessException("INSUFFICIENT_STOCK", "Cantidad no disponible");
        }

        Cart cart = getCart(userId);

        var existing = cart.getItems().stream()
                .filter(i -> i.getProductId().equals(productId))
                .findFirst();

        if (existing.isPresent()) {
            existing.get().setQuantity(existing.get().getQuantity().add(quantity));
        } else {
            CartItem item = CartItem.builder()
                    .cart(cart)
                    .productId(productId)
                    .quantity(quantity)
                    .unitPrice(product.getReferencePrice() != null ? product.getReferencePrice() : BigDecimal.ZERO)
                    .build();
            cart.getItems().add(item);
        }

        return cartRepository.save(cart);
    }

    @Transactional
    public Cart updateItemQuantity(UUID userId, UUID itemId, BigDecimal quantity) {
        Cart cart = getCart(userId);
        cart.getItems().stream().filter(i -> i.getId().equals(itemId)).findFirst()
                .ifPresentOrElse(i -> i.setQuantity(quantity),
                        () -> { throw new BusinessException("ITEM_NOT_FOUND", "Item no encontrado", 404); });
        return cartRepository.save(cart);
    }

    @Transactional
    public Cart removeItem(UUID userId, UUID itemId) {
        Cart cart = getCart(userId);
        cart.getItems().removeIf(i -> i.getId().equals(itemId));
        return cartRepository.save(cart);
    }

    @Transactional
    public void clearCart(UUID userId) {
        cartRepository.findByUserIdAndStatus(userId, "ACTIVE").ifPresent(cartRepository::delete);
    }

    @Transactional
    public Order checkout(UUID userId, String country, String email, String phone, String address) {
        Cart cart = getCart(userId);
        if (cart.getItems().isEmpty()) {
            throw new BusinessException("EMPTY_CART", "El carrito esta vacio");
        }

        Order order = Order.builder()
                .userId(userId)
                .totalAmount(cart.getTotal())
                .currency("COP")
                .status("PENDING")
                .country(country)
                .shippingAddress(address)
                .contactEmail(email)
                .contactPhone(phone)
                .build();

        cart.getItems().forEach(item -> {
            OrderItem oi = OrderItem.builder()
                    .order(order)
                    .productId(item.getProductId())
                    .productName("")
                    .quantity(item.getQuantity())
                    .unitPrice(item.getUnitPrice())
                    .subtotal(item.getUnitPrice().multiply(item.getQuantity()))
                    .build();
            order.getItems().add(oi);
        });

        OrderStatusHistory statusEntry = OrderStatusHistory.builder()
                .order(order)
                .status("PENDING")
                .changedBy(userId)
                .notes("Pedido creado desde el carrito")
                .build();
        order.getStatusHistory().add(statusEntry);

        orderRepository.save(order);
        cart.setStatus("CONVERTED");
        cartRepository.save(cart);

        return order;
    }
}
