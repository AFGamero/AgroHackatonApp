package com.agrotrace.agrotrace.modules.lots.application.service;

import com.agrotrace.agrotrace.modules.farms.domain.repository.FarmRepository;
import com.agrotrace.agrotrace.modules.lots.application.dto.CreateLotDTO;
import com.agrotrace.agrotrace.modules.lots.application.dto.LotResponseDTO;
import com.agrotrace.agrotrace.modules.lots.application.mapper.LotMapper;
import com.agrotrace.agrotrace.modules.lots.domain.model.Lot;
import com.agrotrace.agrotrace.modules.lots.domain.repository.LotRepository;
import com.agrotrace.agrotrace.shared.exceptions.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LotServiceTest {

    @Mock private LotRepository lotRepository;
    @Mock private FarmRepository farmRepository;
    @Mock private LotMapper lotMapper;

    @InjectMocks
    private LotService lotService;

    private UUID farmId, lotId;
    private CreateLotDTO createLotDTO;
    private Lot lot;

    @BeforeEach
    void setUp() {
        farmId = UUID.randomUUID();
        lotId = UUID.randomUUID();
        createLotDTO = new CreateLotDTO("L001", "Lote Cafetero 1", new BigDecimal("5.5"), "Cafe", "Arabica", LocalDate.of(2026, 1, 15), "Lote principal");

        lot = new Lot();
        lot.setId(lotId);
        lot.setFarmId(farmId);
        lot.setCode("L001");
        lot.setName("Lote Cafetero 1");
        lot.setAreaHectares(new BigDecimal("5.5"));
        lot.setCrop("Cafe");
        lot.setVariety("Arabica");
        lot.setPlantingDate(LocalDate.of(2026, 1, 15));
        lot.setDescription("Lote principal");
        lot.setStatus("ACTIVE");
    }

    @Test
    void createLot_shouldReturnLotResponseDTO() {
        when(farmRepository.existsById(farmId)).thenReturn(true);
        when(lotRepository.existsByFarmIdAndCode(farmId, "L001")).thenReturn(false);
        when(lotMapper.toEntity(createLotDTO)).thenReturn(lot);
        when(lotRepository.save(any(Lot.class))).thenReturn(lot);
        when(lotMapper.toResponseDTO(lot)).thenReturn(
                new LotResponseDTO(lotId, farmId, "L001", "Lote Cafetero 1", new BigDecimal("5.5"), "Cafe", "Arabica", LocalDate.of(2026, 1, 15), null, "Lote principal", "ACTIVE"));

        LotResponseDTO result = lotService.createLot(farmId, createLotDTO);
        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo("Lote Cafetero 1");
        verify(lotRepository).save(any(Lot.class));
    }

    @Test
    void createLot_duplicateCode_shouldThrow() {
        when(farmRepository.existsById(farmId)).thenReturn(true);
        when(lotRepository.existsByFarmIdAndCode(farmId, "L001")).thenReturn(true);
        assertThatThrownBy(() -> lotService.createLot(farmId, createLotDTO))
                .isInstanceOf(BusinessException.class);
    }

    @Test
    void getLotsByFarm_shouldReturnList() {
        when(lotRepository.findByFarmId(farmId)).thenReturn(List.of(lot));
        when(lotMapper.toResponseDTO(lot)).thenReturn(
                new LotResponseDTO(lotId, farmId, "L001", "Lote Cafetero 1", new BigDecimal("5.5"), "Cafe", "Arabica", LocalDate.of(2026, 1, 15), null, "Lote principal", "ACTIVE"));

        var result = lotService.getLotsByFarm(farmId);
        assertThat(result).hasSize(1);
    }

    @Test
    void deactivateLot_shouldSetStatusInactive() {
        when(lotRepository.findById(lotId)).thenReturn(Optional.of(lot));
        when(lotRepository.save(lot)).thenReturn(lot);

        lotService.deactivateLot(lotId);
        assertThat(lot.getStatus()).isEqualTo("INACTIVE");
    }
}
