package com.agrotrace.agrotrace.modules.products.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "lot_id", nullable = false)
    private UUID lotId;

    @Column(name = "public_id", nullable = false, unique = true)
    private String publicId;

    @Column(nullable = false, length = 140)
    private String name;

    @Column(length = 1500)
    private String description;

    @Column(nullable = false)
    private String crop;

    @Column
    private String variety;

    @Column(name = "quantity_available", nullable = false, precision = 10, scale = 3)
    private BigDecimal quantityAvailable;

    @Column(nullable = false)
    private String unit;

    @Column(name = "reference_price", precision = 12, scale = 2)
    private BigDecimal referencePrice;

    @Column(length = 3)
    @Builder.Default
    private String currency = "COP";

    @Column(name = "availability_date")
    private LocalDate availabilityDate;

    @Column(nullable = false)
    @Builder.Default
    private String status = "BORRADOR";

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @Column(name = "created_by", nullable = false)
    private UUID createdBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ProductPhoto> photos = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
