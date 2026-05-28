package com.agrotrace.agrotrace.modules.farms.application.service;

import com.agrotrace.agrotrace.modules.farms.application.dto.CreateFarmDTO;
import com.agrotrace.agrotrace.modules.farms.application.dto.FarmResponseDTO;
import com.agrotrace.agrotrace.modules.farms.application.mapper.FarmMapper;
import com.agrotrace.agrotrace.modules.farms.domain.model.Farm;
import com.agrotrace.agrotrace.modules.farms.domain.repository.FarmRepository;
import com.agrotrace.agrotrace.modules.producers.domain.model.Producer;
import com.agrotrace.agrotrace.modules.producers.domain.repository.ProducerRepository;
import com.agrotrace.agrotrace.shared.exceptions.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FarmServiceTest {

    @Mock private FarmRepository farmRepository;
    @Mock private FarmMapper farmMapper;
    @Mock private ProducerRepository producerRepository;

    @InjectMocks
    private FarmService farmService;

    private UUID producerId;
    private UUID farmId;
    private CreateFarmDTO createFarmDTO;
    private Farm farm;
    private Producer producer;

    @BeforeEach
    void setUp() {
        producerId = UUID.randomUUID();
        farmId = UUID.randomUUID();
        createFarmDTO = new CreateFarmDTO("Finca El Paraiso", "Santa Marta", null, null, new BigDecimal("10.5"), "Finca de cafe");

        producer = new Producer();
        producer.setId(producerId);
        producer.setStatus("ACTIVE");

        farm = new Farm();
        farm.setId(farmId);
        farm.setProducerId(producerId);
        farm.setName("Finca El Paraiso");
        farm.setLocation("Santa Marta");
        farm.setAreaHectares(new BigDecimal("10.5"));
        farm.setDescription("Finca de cafe");
        farm.setStatus("ACTIVE");
    }

    @Test
    void createFarm_shouldReturnFarmResponseDTO() {
        when(producerRepository.existsById(producerId)).thenReturn(true);
        when(producerRepository.findById(producerId)).thenReturn(Optional.of(producer));
        when(farmMapper.toEntity(createFarmDTO)).thenReturn(farm);
        when(farmRepository.save(any(Farm.class))).thenReturn(farm);
        when(farmMapper.toResponseDTO(farm)).thenReturn(
                new FarmResponseDTO(farmId, producerId, "Finca El Paraiso", "Santa Marta", null, null, new BigDecimal("10.5"), "Finca de cafe", "ACTIVE"));

        FarmResponseDTO result = farmService.createFarm(producerId, createFarmDTO);

        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo("Finca El Paraiso");
        verify(farmRepository).save(any(Farm.class));
    }

    @Test
    void createFarm_producerNotFound_shouldThrowException() {
        when(producerRepository.existsById(producerId)).thenReturn(false);

        assertThatThrownBy(() -> farmService.createFarm(producerId, createFarmDTO))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Productor no encontrado");
    }

    @Test
    void getFarmById_shouldReturnFarm() {
        when(farmRepository.findById(farmId)).thenReturn(Optional.of(farm));
        when(farmMapper.toResponseDTO(farm)).thenReturn(
                new FarmResponseDTO(farmId, producerId, "Finca El Paraiso", "Santa Marta", null, null, new BigDecimal("10.5"), "Finca de cafe", "ACTIVE"));

        FarmResponseDTO result = farmService.getFarmById(farmId);

        assertThat(result).isNotNull();
        assertThat(result.location()).isEqualTo("Santa Marta");
    }

    @Test
    void getAllFarms_shouldReturnList() {
        when(farmRepository.findAll()).thenReturn(List.of(farm));
        when(farmMapper.toResponseDTO(farm)).thenReturn(
                new FarmResponseDTO(farmId, producerId, "Finca El Paraiso", "Santa Marta", null, null, new BigDecimal("10.5"), "Finca de cafe", "ACTIVE"));

        var result = farmService.getAllFarms();

        assertThat(result).hasSize(1);
    }

    @Test
    void deactivateFarm_shouldSetStatusInactive() {
        when(farmRepository.findById(farmId)).thenReturn(Optional.of(farm));
        when(farmRepository.save(farm)).thenReturn(farm);

        farmService.deactivateFarm(farmId);

        assertThat(farm.getStatus()).isEqualTo("INACTIVE");
    }
}
