package com.agrotrace.agrotrace.modules.lots.domain.model;

import com.agrotrace.agrotrace.shared.audit.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "lots")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lot extends AuditableEntity {

    @Column(name = "farm_id", nullable = false)
    private UUID farmId;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(name = "area_hectares", nullable = false, precision = 10, scale = 2)
    private BigDecimal areaHectares;

    @Column(nullable = false)
    private String crop;

    @Column
    private String variety;

    @Column(name = "planting_date", nullable = false)
    private LocalDate plantingDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "current_status", nullable = false)
    @Builder.Default
    private CertificationStatus currentStatus = CertificationStatus.NO_CERTIFICADO;

    @Column
    private String description;

    @Column(nullable = false)
    @Builder.Default
    private String status = "ACTIVE";
}
