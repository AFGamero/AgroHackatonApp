package com.agrotrace.agrotrace.modules.cart.domain.repository;

import com.agrotrace.agrotrace.modules.cart.domain.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {
    Optional<Cart> findByUserIdAndStatus(UUID userId, String status);
}
