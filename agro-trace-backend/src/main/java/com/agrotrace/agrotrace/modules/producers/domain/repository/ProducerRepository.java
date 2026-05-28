package com.agrotrace.agrotrace.modules.producers.domain.repository;

import com.agrotrace.agrotrace.modules.producers.domain.model.Producer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface ProducerRepository extends JpaRepository<Producer, UUID> {
    Optional<Producer> findByUserId(UUID userId);
    boolean existsByUserId(UUID userId);
    boolean existsByDocumentNumber(String documentNumber);
}
