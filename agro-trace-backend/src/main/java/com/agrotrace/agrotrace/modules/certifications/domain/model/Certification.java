package com.agrotrace.agrotrace.modules.certifications.domain.model;

import com.agrotrace.agrotrace.shared.audit.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "certifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Certification extends AuditableEntity {

    @Column(nullable = false)
    private String type;

    @Column(name = "certifying_entity", nullable = false)
    private String certifyingEntity;

    @Column(name = "certification_number")
    private String certificationNumber;

    @Column(name = "issue_date", nullable = false)
    private LocalDate issueDate;

    @Column(name = "expiration_date", nullable = false)
    private LocalDate expirationDate;

    @Column(nullable = false)
    private String scope;

    @Column(name = "document_url")
    private String documentUrl;

    @Column(nullable = false)
    private String status;

    @Column(name = "farm_id")
    private UUID farmId;

    @Column(name = "lot_id")
    private UUID lotId;
}
