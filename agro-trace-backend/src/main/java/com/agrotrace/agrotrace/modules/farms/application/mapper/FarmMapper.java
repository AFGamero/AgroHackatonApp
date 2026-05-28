package com.agrotrace.agrotrace.modules.farms.application.mapper;

import com.agrotrace.agrotrace.modules.farms.application.dto.CreateFarmDTO;
import com.agrotrace.agrotrace.modules.farms.application.dto.FarmResponseDTO;
import com.agrotrace.agrotrace.modules.farms.domain.model.Farm;
import org.mapstruct.Mapper;

@Mapper(config = com.agrotrace.agrotrace.config.MapperConfig.class)
public interface FarmMapper {

    FarmResponseDTO toResponseDTO(Farm farm);

    default Farm toEntity(CreateFarmDTO dto) {
        Farm farm = new Farm();
        farm.setName(dto.name());
        farm.setLocation(dto.location());
        farm.setLatitude(dto.latitude());
        farm.setLongitude(dto.longitude());
        farm.setAreaHectares(dto.areaHectares());
        farm.setDescription(dto.description());
        return farm;
    }
}
