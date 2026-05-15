package com.academy.eventhub.service;

import com.academy.eventhub.dto.TicketRequestDTO;
import com.academy.eventhub.dto.TicketResponseDTO;
import com.academy.eventhub.entity.*;
import com.academy.eventhub.exception.BusinessException;
import com.academy.eventhub.exception.ResourceNotFoundException;
import com.academy.eventhub.mapper.TicketMapper;
import com.academy.eventhub.repository.EventRepository;
import com.academy.eventhub.repository.TicketRepository;
import com.academy.eventhub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final TicketMapper ticketMapper;

    public TicketResponseDTO bookTicket(TicketRequestDTO dto, int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Utente non trovato con id: " + userId));

        if (user.getStatus() == User.Status.BANNED) {
            throw new BusinessException("Un utente bannato non può prenotare biglietti");
        }

        Event event = eventRepository.findById(dto.getEventId())
                .orElseThrow(() -> new ResourceNotFoundException("Evento non trovato con id: " + dto.getEventId()));

        // l'evento non deve essere già iniziato
        if (!event.getStartDate().isAfter(LocalDateTime.now())) {
            throw new BusinessException("Non puoi prenotare un evento già iniziato o passato");
        }

        // l'utente non può prenotare il proprio evento
        if (event.getOrganizer().getId() == userId) {
            throw new BusinessException("Non puoi prenotare un evento che hai organizzato tu stesso");
        }

        // no doppia prenotazione
        if (ticketRepository.existsByUserIdAndEventId(userId, event.getId())) {
            throw new BusinessException("Hai già un biglietto per questo evento");
        }

        // controllo posti disponibili
        int bookedSeats = ticketRepository.countByEventIdAndStatus(event.getId(), Ticket.TicketStatus.ACTIVE);
        if (bookedSeats >= event.getVenue().getCapacity()) {
            throw new BusinessException("Non ci sono più posti disponibili per questo evento");
        }

        Ticket ticket = new Ticket();
        ticket.setUser(user);
        ticket.setEvent(event);
        ticket.setType(dto.getType());
        ticket.setStatus(Ticket.TicketStatus.ACTIVE);
        ticket.setPrice(dto.getType() == Ticket.TicketType.VIP ? event.getVipPrice() : event.getStandardPrice());

        return ticketMapper.toResponseDTO(ticketRepository.save(ticket));
    }

    public void cancelTicket(int ticketId, int userId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Biglietto non trovato con id: " + ticketId));

        if (ticket.getUser().getId() != userId) {
            throw new BusinessException("Non puoi cancellare il biglietto di un altro utente");
        }

        if (ticket.getStatus() == Ticket.TicketStatus.CANCELLED) {
            throw new BusinessException("Il biglietto è già stato cancellato");
        }

        if (!LocalDateTime.now().isBefore(ticket.getEvent().getStartDate())) {
            throw new BusinessException("Non puoi cancellare un biglietto dopo l'inizio dell'evento");
        }

        ticket.setStatus(Ticket.TicketStatus.CANCELLED);
        ticketRepository.save(ticket);
    }

    public List<TicketResponseDTO> getUserTickets(int userId) {
        return ticketRepository.findByUserId(userId)
                .stream()
                .map(ticketMapper::toResponseDTO)
                .toList();
    }

    public List<TicketResponseDTO> getEventParticipants(int eventId, int requestingUserId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Evento non trovato con id: " + eventId));

        User requester = userRepository.findById(requestingUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Utente non trovato con id: " + requestingUserId));

        boolean isOwner = event.getOrganizer().getId() == requestingUserId;
        boolean isAdmin = requester.getRole() == User.Role.ADMIN;

        if (!isOwner && !isAdmin) {
            throw new BusinessException("Non hai i permessi per vedere i partecipanti di questo evento");
        }

        return ticketRepository.findByEventId(eventId)
                .stream()
                .map(ticketMapper::toResponseDTO)
                .toList();
    }
}