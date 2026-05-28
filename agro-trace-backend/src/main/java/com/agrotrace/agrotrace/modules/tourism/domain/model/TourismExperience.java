package com.agrotrace.agrotrace.modules.tourism.domain.model;

import com.agrotrace.agrotrace.shared.audit.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "tourism_experiences")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TourismExperience extends AuditableEntity {

    @Column(name = "farm_id", nullable = false)
    private UUID farmId;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer capacity;

    @Column
    private String availability;

    @Column(name = "is_published", nullable = false)
    @Builder.Default
    private Boolean isPublished = false;
}
