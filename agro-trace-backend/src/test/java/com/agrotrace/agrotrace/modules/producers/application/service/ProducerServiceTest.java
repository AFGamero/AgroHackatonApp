package com.agrotrace.agrotrace.modules.producers.application.service;

import com.agrotrace.agrotrace.modules.producers.application.dto.CreateProducerDTO;
import com.agrotrace.agrotrace.modules.producers.application.dto.ProducerResponseDTO;
import com.agrotrace.agrotrace.modules.producers.application.mapper.ProducerMapper;
import com.agrotrace.agrotrace.modules.producers.domain.model.Producer;
import com.agrotrace.agrotrace.modules.producers.domain.repository.ProducerRepository;
import com.agrotrace.agrotrace.modules.users.domain.model.User;
import com.agrotrace.agrotrace.modules.users.domain.model.UserRole;
import com.agrotrace.agrotrace.modules.users.domain.model.UserStatus;
import com.agrotrace.agrotrace.modules.users.domain.repository.UserRepository;
import com.agrotrace.agrotrace.shared.exceptions.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProducerServiceTest {

    @Mock
    private ProducerRepository producerRepository;

    @Mock
    private ProducerMapper producerMapper;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProducerService producerService;

    private UUID userId;
    private UUID producerId;
    private CreateProducerDTO createProducerDTO;
    private Producer producer;
    private User user;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        producerId = UUID.randomUUID();

        createProducerDTO = new CreateProducerDTO("CC", "123456789", "3001234567", "Finca El Paraiso");

        user = new User();
        user.setId(userId);
        user.setFullName("Juan Perez");
        user.setEmail("juan@test.com");
        user.setRole(UserRole.PRODUCTOR);
        user.setStatus(UserStatus.ACTIVE);

        producer = new Producer();
        producer.setId(producerId);
        producer.setUserId(userId);
        producer.setDocumentType("CC");
        producer.setDocumentNumber("123456789");
        producer.setPhone("3001234567");
        producer.setOrganization("Finca El Paraiso");
        producer.setStatus("ACTIVE");
    }

    @Test
    void createProducer_shouldReturnProducerResponseDTO() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(producerRepository.existsByUserId(userId)).thenReturn(false);
        when(producerRepository.existsByDocumentNumber(createProducerDTO.documentNumber())).thenReturn(false);
        when(producerMapper.toEntity(createProducerDTO)).thenReturn(producer);
        when(producerRepository.save(any(Producer.class))).thenReturn(producer);
        when(producerMapper.toResponseDTO(producer)).thenReturn(
                new ProducerResponseDTO(producerId, userId, "CC", "123456789", "3001234567", "Finca El Paraiso", "ACTIVE"));

        ProducerResponseDTO result = producerService.createProducer(userId, createProducerDTO);

        assertThat(result).isNotNull();
        assertThat(result.documentNumber()).isEqualTo("123456789");
        assertThat(result.organization()).isEqualTo("Finca El Paraiso");
        verify(producerRepository).save(any(Producer.class));
    }

    @Test
    void createProducer_userAlreadyHasProducer_shouldThrowException() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(producerRepository.existsByUserId(userId)).thenReturn(true);

        assertThatThrownBy(() -> producerService.createProducer(userId, createProducerDTO))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("El usuario ya tiene un perfil de productor");

        verify(producerRepository, never()).save(any());
    }

    @Test
    void createProducer_duplicateDocument_shouldThrowException() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(producerRepository.existsByUserId(userId)).thenReturn(false);
        when(producerRepository.existsByDocumentNumber(createProducerDTO.documentNumber())).thenReturn(true);

        assertThatThrownBy(() -> producerService.createProducer(userId, createProducerDTO))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("El numero de documento ya esta registrado");

        verify(producerRepository, never()).save(any());
    }

    @Test
    void getProducerById_shouldReturnProducer() {
        when(producerRepository.findById(producerId)).thenReturn(Optional.of(producer));
        when(producerMapper.toResponseDTO(producer)).thenReturn(
                new ProducerResponseDTO(producerId, userId, "CC", "123456789", "3001234567", "Finca El Paraiso", "ACTIVE"));

        ProducerResponseDTO result = producerService.getProducerById(producerId);

        assertThat(result).isNotNull();
        assertThat(result.phone()).isEqualTo("3001234567");
    }

    @Test
    void getProducerByUserId_shouldReturnProducer() {
        when(producerRepository.findByUserId(userId)).thenReturn(Optional.of(producer));
        when(producerMapper.toResponseDTO(producer)).thenReturn(
                new ProducerResponseDTO(producerId, userId, "CC", "123456789", "3001234567", "Finca El Paraiso", "ACTIVE"));

        ProducerResponseDTO result = producerService.getProducerByUserId(userId);

        assertThat(result).isNotNull();
        assertThat(result.userId()).isEqualTo(userId);
    }

    @Test
    void getAllProducers_shouldReturnList() {
        when(producerRepository.findAll()).thenReturn(java.util.List.of(producer));
        when(producerMapper.toResponseDTO(producer)).thenReturn(
                new ProducerResponseDTO(producerId, userId, "CC", "123456789", "3001234567", "Finca El Paraiso", "ACTIVE"));

        var result = producerService.getAllProducers();

        assertThat(result).hasSize(1);
    }

    @Test
    void deactivateProducer_shouldSetStatusInactive() {
        when(producerRepository.findById(producerId)).thenReturn(Optional.of(producer));
        when(producerRepository.save(producer)).thenReturn(producer);

        producerService.deactivateProducer(producerId);

        assertThat(producer.getStatus()).isEqualTo("INACTIVE");
        verify(producerRepository).save(producer);
    }
}
