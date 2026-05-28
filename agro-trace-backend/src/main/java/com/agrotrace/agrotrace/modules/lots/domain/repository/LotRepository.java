package com.agrotrace.agrotrace.modules.lots.domain.repository;

import com.agrotrace.agrotrace.modules.lots.domain.model.Lot;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface LotRepository extends JpaRepository<Lot, UUID> {
    List<Lot> findByFarmId(UUID farmId);
    boolean existsByFarmIdAndCode(UUID farmId, String code);
}
