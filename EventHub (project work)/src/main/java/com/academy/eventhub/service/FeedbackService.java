package com.academy.eventhub.service;

import com.academy.eventhub.dto.FeedbackRequestDTO;
import com.academy.eventhub.dto.FeedbackResponseDTO;
import com.academy.eventhub.entity.*;
import com.academy.eventhub.exception.BusinessException;
import com.academy.eventhub.exception.ResourceNotFoundException;
import com.academy.eventhub.mapper.FeedbackMapper;
import com.academy.eventhub.repository.EventRepository;
import com.academy.eventhub.repository.FeedbackRepository;
import com.academy.eventhub.repository.TicketRepository;
import com.academy.eventhub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;
    private final FeedbackMapper feedbackMapper;

    public FeedbackResponseDTO leaveFeedback(FeedbackRequestDTO dto, int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Utente non trovato con id: " + userId));

        Event event = eventRepository.findById(dto.getEventId())
                .orElseThrow(() -> new ResourceNotFoundException("Evento non trovato con id: " + dto.getEventId()));

        // l'evento deve essere concluso
        if (!LocalDateTime.now().isAfter(event.getEndDate())) {
            throw new BusinessException("Puoi lasciare un feedback solo dopo la fine dell'evento");
        }

        // l'utente deve avere un biglietto attivo per quell'evento
        ticketRepository.findByUserIdAndEventIdAndStatus(userId, event.getId(), Ticket.TicketStatus.ACTIVE)
                .orElseThrow(() -> new BusinessException("Puoi lasciare un feedback solo se hai partecipato all'evento"));

        // no feedback duplicato
        if (feedbackRepository.existsByUserIdAndEventId(userId, event.getId())) {
            throw new BusinessException("Hai già lasciato un feedback per questo evento");
        }

        Feedback feedback = new Feedback();
        feedback.setUser(user);
        feedback.setEvent(event);
        feedback.setRating(dto.getRating());
        feedback.setComment(dto.getComment());

        return feedbackMapper.toResponseDTO(feedbackRepository.save(feedback));
    }

    public List<FeedbackResponseDTO> getEventFeedbacks(int eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new ResourceNotFoundException("Evento non trovato con id: " + eventId);
        }
        return feedbackRepository.findByEventId(eventId)
                .stream()
                .map(feedbackMapper::toResponseDTO)
                .toList();
    }

    public Double getEventRating(int eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new ResourceNotFoundException("Evento non trovato con id: " + eventId);
        }
        return feedbackRepository.findAverageRatingByEventId(eventId);
    }

    public void deleteFeedback(int feedbackId) {
        if (!feedbackRepository.existsById(feedbackId)) {
            throw new ResourceNotFoundException("Feedback non trovato con id: " + feedbackId);
        }
        feedbackRepository.deleteById(feedbackId);
    }
}