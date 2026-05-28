package com.agrotrace.agrotrace.modules.producers.application.mapper;

import com.agrotrace.agrotrace.modules.producers.application.dto.CreateProducerDTO;
import com.agrotrace.agrotrace.modules.producers.application.dto.ProducerResponseDTO;
import com.agrotrace.agrotrace.modules.producers.domain.model.Producer;
import org.mapstruct.Mapper;

@Mapper(config = com.agrotrace.agrotrace.config.MapperConfig.class)
public interface ProducerMapper {

    ProducerResponseDTO toResponseDTO(Producer producer);

    default Producer toEntity(CreateProducerDTO dto) {
        Producer producer = new Producer();
        producer.setDocumentType(dto.documentType());
        producer.setDocumentNumber(dto.documentNumber());
        producer.setPhone(dto.phone());
        producer.setOrganization(dto.organization());
        return producer;
    }
}
