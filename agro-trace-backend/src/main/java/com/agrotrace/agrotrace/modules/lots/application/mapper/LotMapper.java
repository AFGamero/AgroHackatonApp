package com.agrotrace.agrotrace.modules.lots.application.mapper;

import com.agrotrace.agrotrace.modules.lots.application.dto.CreateLotDTO;
import com.agrotrace.agrotrace.modules.lots.application.dto.LotResponseDTO;
import com.agrotrace.agrotrace.modules.lots.domain.model.Lot;
import org.mapstruct.Mapper;

@Mapper(config = com.agrotrace.agrotrace.config.MapperConfig.class)
public interface LotMapper {

    LotResponseDTO toResponseDTO(Lot lot);

    default Lot toEntity(CreateLotDTO dto) {
        Lot lot = new Lot();
        lot.setCode(dto.code());
        lot.setName(dto.name());
        lot.setAreaHectares(dto.areaHectares());
        lot.setCrop(dto.crop());
        lot.setVariety(dto.variety());
        lot.setPlantingDate(dto.plantingDate());
        lot.setDescription(dto.description());
        return lot;
    }
}
