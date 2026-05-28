package com.agrotrace.agrotrace.modules.lots.application.service;

import com.agrotrace.agrotrace.modules.farms.domain.repository.FarmRepository;
import com.agrotrace.agrotrace.modules.lots.application.dto.CreateLotDTO;
import com.agrotrace.agrotrace.modules.lots.application.dto.LotResponseDTO;
import com.agrotrace.agrotrace.modules.lots.application.mapper.LotMapper;
import com.agrotrace.agrotrace.modules.lots.domain.model.Lot;
import com.agrotrace.agrotrace.modules.lots.domain.repository.LotRepository;
import com.agrotrace.agrotrace.shared.exceptions.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LotService {

    private final LotRepository lotRepository;
    private final FarmRepository farmRepository;
    private final LotMapper lotMapper;

    @Transactional
    public LotResponseDTO createLot(UUID farmId, CreateLotDTO dto) {
        if (!farmRepository.existsById(farmId)) {
            throw new BusinessException("FARM_NOT_FOUND", "Finca no encontrada", 404);
        }
        if (lotRepository.existsByFarmIdAndCode(farmId, dto.code())) {
            throw new BusinessException("CODE_EXISTS", "Ya existe un lote con ese codigo en esta finca");
        }

        Lot lot = lotMapper.toEntity(dto);
        lot.setFarmId(farmId);
        lot = lotRepository.save(lot);
        return lotMapper.toResponseDTO(lot);
    }

    public LotResponseDTO getLotById(UUID id) {
        return lotRepository.findById(id)
                .map(lotMapper::toResponseDTO)
                .orElseThrow(() -> new BusinessException("LOT_NOT_FOUND", "Lote no encontrado", 404));
    }

    public List<LotResponseDTO> getLotsByFarm(UUID farmId) {
        return lotRepository.findByFarmId(farmId).stream()
                .map(lotMapper::toResponseDTO)
                .toList();
    }

    public List<LotResponseDTO> getAllLots() {
        return lotRepository.findAll().stream()
                .map(lotMapper::toResponseDTO)
                .toList();
    }

    @Transactional
    public LotResponseDTO updateLot(UUID id, CreateLotDTO dto) {
        Lot lot = lotRepository.findById(id)
                .orElseThrow(() -> new BusinessException("LOT_NOT_FOUND", "Lote no encontrado", 404));

        if (dto.code() != null) lot.setCode(dto.code());
        if (dto.name() != null) lot.setName(dto.name());
        if (dto.areaHectares() != null) lot.setAreaHectares(dto.areaHectares());
        if (dto.crop() != null) lot.setCrop(dto.crop());
        if (dto.variety() != null) lot.setVariety(dto.variety());
        if (dto.plantingDate() != null) lot.setPlantingDate(dto.plantingDate());
        if (dto.description() != null) lot.setDescription(dto.description());

        lot = lotRepository.save(lot);
        return lotMapper.toResponseDTO(lot);
    }

    @Transactional
    public void deactivateLot(UUID id) {
        Lot lot = lotRepository.findById(id)
                .orElseThrow(() -> new BusinessException("LOT_NOT_FOUND", "Lote no encontrado", 404));
        lot.setStatus("INACTIVE");
        lotRepository.save(lot);
    }
}
