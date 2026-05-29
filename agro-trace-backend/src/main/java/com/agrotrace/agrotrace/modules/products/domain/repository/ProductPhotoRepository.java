package com.agrotrace.agrotrace.modules.products.domain.repository;

import com.agrotrace.agrotrace.modules.products.domain.model.ProductPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProductPhotoRepository extends JpaRepository<ProductPhoto, UUID> {
    List<ProductPhoto> findByProductId(UUID productId);
}
