package com.agrotrace.agrotrace.modules.products.domain.repository;

import com.agrotrace.agrotrace.modules.products.domain.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findByCreatedBy(UUID userId);
    List<Product> findByLotId(UUID lotId);

    @Query("SELECT p FROM Product p WHERE p.status = 'PUBLICADO'")
    Page<Product> findAllPublished(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.status = 'PUBLICADO' AND " +
           "(:search IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(p.crop) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Product> findPublishedWithSearch(String search, Pageable pageable);
}
