package com.academy.eventhub.mapper;

import com.academy.eventhub.dto.UserProfileRequestDTO;
import com.academy.eventhub.dto.UserProfileResponseDTO;
import com.academy.eventhub.entity.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    UserProfileResponseDTO toResponseDTO(UserProfile profile);
    UserProfile toEntity(UserProfileRequestDTO dto);
    void updateEntity(UserProfileRequestDTO dto, @MappingTarget UserProfile profile);
}