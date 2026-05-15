package com.academy.eventhub.mapper;

import com.academy.eventhub.dto.TagRequestDTO;
import com.academy.eventhub.dto.TagResponseDTO;
import com.academy.eventhub.entity.Tag;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TagMapper {
    TagResponseDTO toResponseDTO(Tag tag);
    Tag toEntity(TagRequestDTO dto);
}