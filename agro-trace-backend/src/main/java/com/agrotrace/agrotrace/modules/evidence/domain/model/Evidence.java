package com.agrotrace.agrotrace.modules.evidence.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "evidence")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Evidence {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "lot_id", nullable = false)
    private UUID lotId;

    @Column(name = "certification_status_event_id")
    private UUID certificationStatusEventId;

    @Column(nullable = false)
    private String type;

    @Column
    private String url;

    @Column
    private String comment;

    @Column
    private String description;

    @Column(name = "visible_publicly", nullable = false)
    @Builder.Default
    private Boolean visiblePublicly = true;

    @Column(name = "registered_by", nullable = false)
    private UUID registeredBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
