package com.agrotrace.agrotrace.modules.farms.domain.model;

import com.agrotrace.agrotrace.shared.audit.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "farms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Farm extends AuditableEntity {

    @Column(name = "producer_id", nullable = false)
    private UUID producerId;

    @Column(nullable = false)
    private String name;

    @Column
    private String location;

    @Column(precision = 10, scale = 8)
    private BigDecimal latitude;

    @Column(precision = 11, scale = 8)
    private BigDecimal longitude;

    @Column(name = "area_hectares", nullable = false, precision = 10, scale = 2)
    private BigDecimal areaHectares;

    @Column
    private String description;

    @Column(nullable = false)
    @Builder.Default
    private String status = "ACTIVE";

    @OneToMany(mappedBy = "farmId", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<FarmPhoto> photos = new ArrayList<>();
}
