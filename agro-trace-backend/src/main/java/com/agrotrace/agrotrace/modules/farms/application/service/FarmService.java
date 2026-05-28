package com.agrotrace.agrotrace.modules.farms.application.service;

import com.agrotrace.agrotrace.modules.farms.application.dto.CreateFarmDTO;
import com.agrotrace.agrotrace.modules.farms.application.dto.FarmResponseDTO;
import com.agrotrace.agrotrace.modules.farms.application.mapper.FarmMapper;
import com.agrotrace.agrotrace.modules.farms.domain.model.Farm;
import com.agrotrace.agrotrace.modules.farms.domain.repository.FarmRepository;
import com.agrotrace.agrotrace.modules.producers.domain.repository.ProducerRepository;
import com.agrotrace.agrotrace.shared.exceptions.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FarmService {

    private final FarmRepository farmRepository;
    private final FarmMapper farmMapper;
    private final ProducerRepository producerRepository;

    @Transactional
    public FarmResponseDTO createFarm(UUID producerId, CreateFarmDTO dto) {
        if (!producerRepository.existsById(producerId)) {
            throw new BusinessException("PRODUCER_NOT_FOUND", "Productor no encontrado", 404);
        }
        if (producerRepository.findById(producerId).orElseThrow().getStatus().equals("INACTIVE")) {
            throw new BusinessException("PRODUCER_INACTIVE", "El productor no esta activo", 400);
        }

        Farm farm = farmMapper.toEntity(dto);
        farm.setProducerId(producerId);
        farm = farmRepository.save(farm);
        return farmMapper.toResponseDTO(farm);
    }

    public FarmResponseDTO getFarmById(UUID id) {
        Farm farm = farmRepository.findById(id)
                .orElseThrow(() -> new BusinessException("FARM_NOT_FOUND", "Finca no encontrada", 404));
        return farmMapper.toResponseDTO(farm);
    }

    public List<FarmResponseDTO> getFarmsByProducer(UUID producerId) {
        return farmRepository.findByProducerId(producerId).stream()
                .map(farmMapper::toResponseDTO)
                .toList();
    }

    public List<FarmResponseDTO> getAllFarms() {
        return farmRepository.findAll().stream()
                .map(farmMapper::toResponseDTO)
                .toList();
    }

    @Transactional
    public FarmResponseDTO updateFarm(UUID id, CreateFarmDTO dto) {
        Farm farm = farmRepository.findById(id)
                .orElseThrow(() -> new BusinessException("FARM_NOT_FOUND", "Finca no encontrada", 404));

        if (dto.name() != null) farm.setName(dto.name());
        if (dto.location() != null) farm.setLocation(dto.location());
        if (dto.latitude() != null) farm.setLatitude(dto.latitude());
        if (dto.longitude() != null) farm.setLongitude(dto.longitude());
        if (dto.areaHectares() != null) farm.setAreaHectares(dto.areaHectares());
        if (dto.description() != null) farm.setDescription(dto.description());

        farm = farmRepository.save(farm);
        return farmMapper.toResponseDTO(farm);
    }

    @Transactional
    public void deactivateFarm(UUID id) {
        Farm farm = farmRepository.findById(id)
                .orElseThrow(() -> new BusinessException("FARM_NOT_FOUND", "Finca no encontrada", 404));
        farm.setStatus("INACTIVE");
        farmRepository.save(farm);
    }
}
