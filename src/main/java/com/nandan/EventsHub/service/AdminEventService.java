package com.nandan.EventsHub.service;

import com.nandan.EventsHub.dto.AdminEventDTO;
import com.nandan.EventsHub.dto.EventAvailabilityResponse;
import com.nandan.EventsHub.dto.EventCreateDTO;
import com.nandan.EventsHub.exception.EventNameAlreadyExistsException;
import com.nandan.EventsHub.exception.ResourceNotFoundException;
import com.nandan.EventsHub.model.Event;
import com.nandan.EventsHub.model.Like;
import com.nandan.EventsHub.repo.CommentRepository;
import com.nandan.EventsHub.repo.EventRepository;
import com.nandan.EventsHub.repo.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminEventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private CommentRepository commentRepository;

    public boolean isEventNameAvailable(String eventName) {
        return !eventRepository.existsByEventNameIgnoreCase(eventName);
    }

    public Event createEvent(EventCreateDTO dto) {
        if (eventRepository.existsByEventNameIgnoreCase(dto.getEventName())) {
            throw new EventNameAlreadyExistsException("Event name already exists: " + dto.getEventName());
        }

        Event event = Event.builder()
                .eventName(dto.getEventName())
                .imageUrl(dto.getImageUrl())
                .location(dto.getLocation())
                .eventType(dto.getEventType())
                .description(dto.getDescription())
                .eventDateTime(dto.getEventDateTime())
                .totalSeats(dto.getTotalSeats())
                .availableSeats(dto.getAvailableSeats())
                .price(dto.getPrice())
                .build();

        return eventRepository.save(event);
    }

    public EventAvailabilityResponse checkEventAvailability(String eventName) {
        boolean exists = eventRepository.existsByEventNameIgnoreCase(eventName);
        if (exists) {
            return new EventAvailabilityResponse(false, "Event name is already taken");
        }
        return new EventAvailabilityResponse(true, "Event name is available");
    }

    public String cancelEventByName(String eventName) {
        Event event = eventRepository.findByEventName(eventName)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with name: " + eventName));

        eventRepository.delete(event);
        return "successfully canceld";
    }

    public List<AdminEventDTO> searchEventsByName(String eventName) {
        List<Event> events = eventRepository.findByEventNameContainingIgnoreCase(eventName);

        if (events.isEmpty()) {
            throw new ResourceNotFoundException("No events found with name: " + eventName);
        }

        return events.stream().map(event -> AdminEventDTO.builder()
                        .imageUrl(event.getImageUrl())
                        .eventName(event.getEventName())
                        .location(event.getLocation())
                        .localDateTime(event.getEventDateTime())
                        .likesCount(likeRepository.countByEvent(event))
                        .commentsCount(commentRepository.countByEvent(event))
                        .build())
                .collect(Collectors.toList());
    }



}
