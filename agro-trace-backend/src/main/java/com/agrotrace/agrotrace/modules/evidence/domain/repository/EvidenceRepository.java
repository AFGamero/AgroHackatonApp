package com.agrotrace.agrotrace.modules.evidence.domain.repository;

import com.agrotrace.agrotrace.modules.evidence.domain.model.Evidence;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface EvidenceRepository extends JpaRepository<Evidence, UUID> {
    List<Evidence> findByLotId(UUID lotId);
}
