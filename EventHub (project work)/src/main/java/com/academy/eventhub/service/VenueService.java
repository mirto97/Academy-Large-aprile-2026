package com.academy.eventhub.service;

import com.academy.eventhub.dto.VenueRequestDTO;
import com.academy.eventhub.dto.VenueResponseDTO;
import com.academy.eventhub.entity.Venue;
import com.academy.eventhub.exception.BusinessException;
import com.academy.eventhub.exception.ResourceNotFoundException;
import com.academy.eventhub.mapper.VenueMapper;
import com.academy.eventhub.repository.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VenueService {

    private final VenueRepository venueRepository;
    private final VenueMapper venueMapper;

    public List<VenueResponseDTO> getAllVenues() {
        return venueRepository.findAll()
                .stream()
                .map(venueMapper::toResponseDTO)
                .toList();
    }

    public VenueResponseDTO getVenueById(int id) {
        return venueMapper.toResponseDTO(findVenueOrThrow(id));
    }

    public VenueResponseDTO createVenue(VenueRequestDTO dto) {
        if (venueRepository.existsByName(dto.getName())) {
            throw new BusinessException("Esiste già una sede con il nome: " + dto.getName());
        }

        return venueMapper.toResponseDTO(venueRepository.save(venueMapper.toEntity(dto)));
    }

    public VenueResponseDTO updateVenue(int id, VenueRequestDTO dto) {
        Venue venue = findVenueOrThrow(id);
        venueMapper.updateEntity(dto, venue);
        return venueMapper.toResponseDTO(venueRepository.save(venue));
    }

    public void deleteVenue(int id) {
        findVenueOrThrow(id);
        venueRepository.deleteById(id);
    }

    private Venue findVenueOrThrow(int id) {
        return venueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sede non trovata con id: " + id));
    }
}