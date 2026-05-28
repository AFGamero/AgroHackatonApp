package com.agrotrace.agrotrace.modules.producers.domain.model;

import com.agrotrace.agrotrace.shared.audit.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "producers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Producer extends AuditableEntity {

    @Column(name = "user_id", nullable = false, unique = true)
    private UUID userId;

    @Column(name = "document_type", nullable = false)
    private String documentType;

    @Column(name = "document_number", nullable = false, unique = true)
    private String documentNumber;

    @Column
    private String phone;

    @Column
    private String organization;

    @Column(nullable = false)
    @Builder.Default
    private String status = "ACTIVE";
}
