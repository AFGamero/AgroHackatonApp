package com.agrotrace.agrotrace.modules.users.application.mapper;

import com.agrotrace.agrotrace.modules.users.application.dto.CreateUserDTO;
import com.agrotrace.agrotrace.modules.users.application.dto.UserResponseDTO;
import com.agrotrace.agrotrace.modules.users.domain.model.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@org.mapstruct.Mapper(config = com.agrotrace.agrotrace.config.MapperConfig.class)
public interface UserMapper {

    UserResponseDTO toResponseDTO(User user);
    
    User toEntity(CreateUserDTO dto);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(CreateUserDTO dto, @MappingTarget User user);
}
