package com.academy.eventhub.service;

import com.academy.eventhub.dto.EventRequestDTO;
import com.academy.eventhub.dto.EventResponseDTO;
import com.academy.eventhub.entity.*;
import com.academy.eventhub.exception.BusinessException;
import com.academy.eventhub.exception.ResourceNotFoundException;
import com.academy.eventhub.mapper.EventMapper;
import com.academy.eventhub.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final VenueRepository venueRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final SpeakerRepository speakerRepository;
    private final TicketRepository ticketRepository;
    private final EventMapper eventMapper;

    public List<EventResponseDTO> getAllEvents() {
        return eventRepository.findAll()
                .stream()
                .map(this::toResponseWithSeats)
                .toList();
    }

    public List<EventResponseDTO> getEventsByOrganizer(int organizerId) {
        return eventRepository.findByOrganizerId(organizerId)
                .stream()
                .map(this::toResponseWithSeats)
                .toList();
    }

    public EventResponseDTO getEventById(int id) {
        return toResponseWithSeats(findEventOrThrow(id));
    }

    public EventResponseDTO createEvent(EventRequestDTO dto, int organizerId) {
        User organizer = userRepository.findById(organizerId)
                .orElseThrow(() -> new ResourceNotFoundException("Utente non trovato con id: " + organizerId));

        if (organizer.getStatus() == User.Status.BANNED) {
            throw new BusinessException("Un utente bannato non può creare eventi");
        }

        Event event = eventMapper.toEntity(dto);
        event.setOrganizer(organizer);
        event.setVenue(findVenueOrThrow(dto.getVenueId()));
        event.setTags(resolveTags(dto.getTagIds()));
        event.setSpeakers(resolveSpeakers(dto.getSpeakerIds()));

        return toResponseWithSeats(eventRepository.save(event));
    }

    public EventResponseDTO updateEvent(int eventId, EventRequestDTO dto, int requestingUserId) {
        Event event = findEventOrThrow(eventId);
        checkOwnershipOrAdmin(event, requestingUserId);

        eventMapper.updateEntity(dto, event);
        event.setVenue(findVenueOrThrow(dto.getVenueId()));
        event.setTags(resolveTags(dto.getTagIds()));
        event.setSpeakers(resolveSpeakers(dto.getSpeakerIds()));

        return toResponseWithSeats(eventRepository.save(event));
    }

    public void deleteEvent(int eventId, int requestingUserId) {
        Event event = findEventOrThrow(eventId);
        checkOwnershipOrAdmin(event, requestingUserId);
        eventRepository.deleteById(eventId);
    }

    // --- Helper privati ---

    private EventResponseDTO toResponseWithSeats(Event event) {
        EventResponseDTO dto = eventMapper.toResponseDTO(event);
        int bookedSeats = ticketRepository.countByEventIdAndStatus(event.getId(), Ticket.TicketStatus.ACTIVE);
        dto.setAvailableSeats(event.getVenue().getCapacity() - bookedSeats);
        return dto;
    }

    private void checkOwnershipOrAdmin(Event event, int requestingUserId) {
        User requester = userRepository.findById(requestingUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Utente non trovato con id: " + requestingUserId));

        boolean isOwner = event.getOrganizer().getId() == requestingUserId;
        boolean isAdmin = requester.getRole() == User.Role.ADMIN;

        if (!isOwner && !isAdmin) {
            throw new BusinessException("Non hai i permessi per modificare questo evento");
        }
    }

    private Venue findVenueOrThrow(int venueId) {
        return venueRepository.findById(venueId)
                .orElseThrow(() -> new ResourceNotFoundException("Sede non trovata con id: " + venueId));
    }

    private List<Tag> resolveTags(List<Integer> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) return List.of();
        return tagIds.stream()
                .map(id -> tagRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Tag non trovato con id: " + id)))
                .toList();
    }

    private List<Speaker> resolveSpeakers(List<Integer> speakerIds) {
        if (speakerIds == null || speakerIds.isEmpty()) return List.of();
        return speakerIds.stream()
                .map(id -> speakerRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Relatore non trovato con id: " + id)))
                .toList();
    }

    private Event findEventOrThrow(int id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evento non trovato con id: " + id));
    }
}