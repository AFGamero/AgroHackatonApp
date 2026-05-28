package com.agrotrace.agrotrace.modules.tourism.domain.repository;

import com.agrotrace.agrotrace.modules.tourism.domain.model.TourismExperience;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface TourismExperienceRepository extends JpaRepository<TourismExperience, UUID> {
    List<TourismExperience> findByFarmId(UUID farmId);
    List<TourismExperience> findByIsPublishedTrue();
}
