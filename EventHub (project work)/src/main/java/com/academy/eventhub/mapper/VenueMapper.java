package com.academy.eventhub.mapper;

import com.academy.eventhub.dto.VenueRequestDTO;
import com.academy.eventhub.dto.VenueResponseDTO;
import com.academy.eventhub.entity.Venue;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface VenueMapper {
    VenueResponseDTO toResponseDTO(Venue venue);
    Venue toEntity(VenueRequestDTO dto);
    void updateEntity(VenueRequestDTO dto, @MappingTarget Venue venue);
}