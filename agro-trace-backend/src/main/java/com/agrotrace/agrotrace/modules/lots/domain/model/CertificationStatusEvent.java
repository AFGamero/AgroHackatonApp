package com.agrotrace.agrotrace.modules.lots.domain.model;

import com.agrotrace.agrotrace.modules.certifications.domain.model.Certification;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "certification_status_events",
       indexes = @Index(name = "idx_cert_status_events_lot_id", columnList = "lot_id"))
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CertificationStatusEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "lot_id", nullable = false)
    private UUID lotId;

    @Enumerated(EnumType.STRING)
    @Column(name = "certification_status", nullable = false)
    private CertificationStatus certificationStatus;

    @Column(name = "event_date", nullable = false)
    private LocalDate eventDate;

    @Column(name = "observations")
    private String observations;

    @Column(name = "registered_by", nullable = false)
    private UUID registeredBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "certification_id", foreignKey = @ForeignKey(name = "fk_cert_status_events_certification"))
    private Certification certification;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
