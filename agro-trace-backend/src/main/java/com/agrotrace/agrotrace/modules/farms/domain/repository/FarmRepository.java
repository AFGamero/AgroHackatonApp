package com.agrotrace.agrotrace.modules.farms.domain.repository;

import com.agrotrace.agrotrace.modules.farms.domain.model.Farm;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface FarmRepository extends JpaRepository<Farm, UUID> {
    List<Farm> findByProducerId(UUID producerId);
    boolean existsByProducerIdAndName(UUID producerId, String name);
}
