package com.academy.eventhub.service;

import com.academy.eventhub.dto.SpeakerRequestDTO;
import com.academy.eventhub.dto.SpeakerResponseDTO;
import com.academy.eventhub.entity.Speaker;
import com.academy.eventhub.exception.ResourceNotFoundException;
import com.academy.eventhub.mapper.SpeakerMapper;
import com.academy.eventhub.repository.SpeakerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpeakerService {

    private final SpeakerRepository speakerRepository;
    private final SpeakerMapper speakerMapper;

    public List<SpeakerResponseDTO> getAllSpeakers() {
        return speakerRepository.findAll()
                .stream()
                .map(speakerMapper::toResponseDTO)
                .toList();
    }

    public SpeakerResponseDTO getSpeakerById(int id) {
        return speakerMapper.toResponseDTO(findSpeakerOrThrow(id));
    }

    public SpeakerResponseDTO createSpeaker(SpeakerRequestDTO dto) {
        return speakerMapper.toResponseDTO(speakerRepository.save(speakerMapper.toEntity(dto)));
    }

    public SpeakerResponseDTO updateSpeaker(int id, SpeakerRequestDTO dto) {
        Speaker speaker = findSpeakerOrThrow(id);
        speakerMapper.updateEntity(dto, speaker);
        return speakerMapper.toResponseDTO(speakerRepository.save(speaker));
    }

    public void deleteSpeaker(int id) {
        findSpeakerOrThrow(id);
        speakerRepository.deleteById(id);
    }

    private Speaker findSpeakerOrThrow(int id) {
        return speakerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Relatore non trovato con id: " + id));
    }
}