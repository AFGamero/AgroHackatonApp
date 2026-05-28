package com.agrotrace.agrotrace.modules.producers.application.service;

import com.agrotrace.agrotrace.modules.producers.application.dto.CreateProducerDTO;
import com.agrotrace.agrotrace.modules.producers.application.dto.ProducerResponseDTO;
import com.agrotrace.agrotrace.modules.producers.application.mapper.ProducerMapper;
import com.agrotrace.agrotrace.modules.producers.domain.model.Producer;
import com.agrotrace.agrotrace.modules.producers.domain.repository.ProducerRepository;
import com.agrotrace.agrotrace.modules.users.domain.model.User;
import com.agrotrace.agrotrace.modules.users.domain.repository.UserRepository;
import com.agrotrace.agrotrace.shared.exceptions.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProducerService {

    private final ProducerRepository producerRepository;
    private final ProducerMapper producerMapper;
    private final UserRepository userRepository;

    @Transactional
    public ProducerResponseDTO createProducer(UUID userId, CreateProducerDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("USER_NOT_FOUND", "Usuario no encontrado", 404));

        if (producerRepository.existsByUserId(userId)) {
            throw new BusinessException("PRODUCER_EXISTS", "El usuario ya tiene un perfil de productor");
        }

        if (producerRepository.existsByDocumentNumber(dto.documentNumber())) {
            throw new BusinessException("DOCUMENT_EXISTS", "El numero de documento ya esta registrado");
        }

        Producer producer = producerMapper.toEntity(dto);
        producer.setUserId(userId);
        producer = producerRepository.save(producer);
        return producerMapper.toResponseDTO(producer);
    }

    public ProducerResponseDTO getProducerById(UUID id) {
        Producer producer = producerRepository.findById(id)
                .orElseThrow(() -> new BusinessException("PRODUCER_NOT_FOUND", "Productor no encontrado", 404));
        return producerMapper.toResponseDTO(producer);
    }

    public ProducerResponseDTO getProducerByUserId(UUID userId) {
        Producer producer = producerRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException("PRODUCER_NOT_FOUND", "Productor no encontrado", 404));
        return producerMapper.toResponseDTO(producer);
    }

    public List<ProducerResponseDTO> getAllProducers() {
        return producerRepository.findAll().stream()
                .map(producerMapper::toResponseDTO)
                .toList();
    }

    @Transactional
    public ProducerResponseDTO updateProducer(UUID id, CreateProducerDTO dto) {
        Producer producer = producerRepository.findById(id)
                .orElseThrow(() -> new BusinessException("PRODUCER_NOT_FOUND", "Productor no encontrado", 404));

        if (dto.documentNumber() != null && !dto.documentNumber().equals(producer.getDocumentNumber())) {
            if (producerRepository.existsByDocumentNumber(dto.documentNumber())) {
                throw new BusinessException("DOCUMENT_EXISTS", "El numero de documento ya esta registrado");
            }
            producer.setDocumentNumber(dto.documentNumber());
        }

        if (dto.documentType() != null) producer.setDocumentType(dto.documentType());
        if (dto.phone() != null) producer.setPhone(dto.phone());
        if (dto.organization() != null) producer.setOrganization(dto.organization());

        producer = producerRepository.save(producer);
        return producerMapper.toResponseDTO(producer);
    }

    @Transactional
    public void deactivateProducer(UUID id) {
        Producer producer = producerRepository.findById(id)
                .orElseThrow(() -> new BusinessException("PRODUCER_NOT_FOUND", "Productor no encontrado", 404));
        producer.setStatus("INACTIVE");
        producerRepository.save(producer);
    }
}
