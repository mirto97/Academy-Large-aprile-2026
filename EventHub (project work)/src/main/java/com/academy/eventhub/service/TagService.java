package com.academy.eventhub.service;

import com.academy.eventhub.dto.TagRequestDTO;
import com.academy.eventhub.dto.TagResponseDTO;
import com.academy.eventhub.exception.BusinessException;
import com.academy.eventhub.exception.ResourceNotFoundException;
import com.academy.eventhub.mapper.TagMapper;
import com.academy.eventhub.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    public List<TagResponseDTO> getAllTags() {
        return tagRepository.findAll()
                .stream()
                .map(tagMapper::toResponseDTO)
                .toList();
    }

    public TagResponseDTO createTag(TagRequestDTO dto) {
        if (tagRepository.existsByName(dto.getName())) {
            throw new BusinessException("Esiste già un tag con il nome: " + dto.getName());
        }

        return tagMapper.toResponseDTO(tagRepository.save(tagMapper.toEntity(dto)));
    }

    public void deleteTag(int id) {
        if (!tagRepository.existsById(id)) {
            throw new ResourceNotFoundException("Tag non trovato con id: " + id);
        }
        tagRepository.deleteById(id);
    }
}