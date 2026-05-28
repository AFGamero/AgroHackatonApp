package com.agrotrace.agrotrace.modules.tourism.application.service;

import com.agrotrace.agrotrace.modules.farms.domain.repository.FarmRepository;
import com.agrotrace.agrotrace.modules.tourism.application.dto.*;
import com.agrotrace.agrotrace.modules.tourism.domain.model.TourismExperience;
import com.agrotrace.agrotrace.modules.tourism.domain.repository.TourismExperienceRepository;
import com.agrotrace.agrotrace.shared.exceptions.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TourismService {

    private final TourismExperienceRepository experienceRepository;
    private final FarmRepository farmRepository;

    @Transactional
    public ExperienceResponseDTO createExperience(UUID farmId, CreateExperienceDTO dto) {
        if (!farmRepository.existsById(farmId)) {
            throw new BusinessException("FARM_NOT_FOUND", "Finca no encontrada", 404);
        }

        TourismExperience exp = new TourismExperience();
        exp.setFarmId(farmId);
        exp.setName(dto.name());
        exp.setDescription(dto.description());
        exp.setDurationMinutes(dto.durationMinutes());
        exp.setPrice(dto.price());
        exp.setCapacity(dto.capacity());
        exp.setAvailability(dto.availability());

        return toDTO(experienceRepository.save(exp));
    }

    public List<ExperienceResponseDTO> getByFarm(UUID farmId) {
        return experienceRepository.findByFarmId(farmId).stream().map(this::toDTO).toList();
    }

    public List<ExperienceResponseDTO> getPublicExperiences() {
        return experienceRepository.findByIsPublishedTrue().stream().map(this::toDTO).toList();
    }

    public ExperienceResponseDTO getById(UUID id) {
        return toDTO(experienceRepository.findById(id)
                .orElseThrow(() -> new BusinessException("EXPERIENCE_NOT_FOUND", "Experiencia no encontrada", 404)));
    }

    @Transactional
    public ExperienceResponseDTO updateExperience(UUID id, CreateExperienceDTO dto) {
        TourismExperience exp = experienceRepository.findById(id)
                .orElseThrow(() -> new BusinessException("EXPERIENCE_NOT_FOUND", "Experiencia no encontrada", 404));

        if (dto.name() != null) exp.setName(dto.name());
        if (dto.description() != null) exp.setDescription(dto.description());
        if (dto.durationMinutes() != null) exp.setDurationMinutes(dto.durationMinutes());
        if (dto.price() != null) exp.setPrice(dto.price());
        if (dto.capacity() != null) exp.setCapacity(dto.capacity());
        if (dto.availability() != null) exp.setAvailability(dto.availability());

        return toDTO(experienceRepository.save(exp));
    }

    @Transactional
    public ExperienceResponseDTO publishExperience(UUID id) {
        TourismExperience exp = experienceRepository.findById(id)
                .orElseThrow(() -> new BusinessException("EXPERIENCE_NOT_FOUND", "Experiencia no encontrada", 404));

        exp.setIsPublished(true);
        return toDTO(experienceRepository.save(exp));
    }

    @Transactional
    public void deleteExperience(UUID id) {
        if (!experienceRepository.existsById(id)) {
            throw new BusinessException("EXPERIENCE_NOT_FOUND", "Experiencia no encontrada", 404);
        }
        experienceRepository.deleteById(id);
    }

    private ExperienceResponseDTO toDTO(TourismExperience e) {
        return new ExperienceResponseDTO(e.getId(), e.getFarmId(), e.getName(), e.getDescription(), e.getDurationMinutes(), e.getPrice(), e.getCapacity(), e.getAvailability(), e.getIsPublished());
    }
}
