package com.nandan.EventsHub.service;

import com.nandan.EventsHub.dto.*;
import com.nandan.EventsHub.exception.ResourceAlreadyExistsException;
import com.nandan.EventsHub.exception.ResourceNotFoundException;
import com.nandan.EventsHub.model.Comment;
import com.nandan.EventsHub.model.Event;
import com.nandan.EventsHub.model.Like;
import com.nandan.EventsHub.model.User;
import com.nandan.EventsHub.repo.CommentRepository;
import com.nandan.EventsHub.repo.EventRepository;
import com.nandan.EventsHub.repo.LikeRepository;
import com.nandan.EventsHub.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    public Event createEvent(EventRequestDTO dto) {

        eventRepository.findByEventName(dto.getEventName())
                .ifPresent(e -> {
                    throw new ResourceAlreadyExistsException("Event already exists with name: " + dto.getEventName());
                });

        // Convert DTO to Entity
        Event event = Event.builder()
                .eventName(dto.getEventName())
                .imageUrl(dto.getImageUrl())
                .location(dto.getLocation())
                .eventType(dto.getEventType())
                .eventDateTime(dto.getEventDateTime())
                .totalSeats(dto.getTotalSeats())
                .availableSeats(dto.getAvailableSeats())
                .price(dto.getPrice())
                .build();

        // Save in database
        return eventRepository.save(event);
    }

    public ResponseEntity<Map<String, Object>> isEventNameAvailable(String eventName) {
        boolean available =  eventRepository.findByEventName(eventName).isEmpty();

        Map<String, Object> response = new HashMap<>();
        response.put("eventName", eventName);

        if (available) {
            response.put("available", true);
            return ResponseEntity.ok(response); // 200 OK
        } else {
            response.put("available", false);
            response.put("message", "Event name already exists");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response); // 409 Conflict
        }
    }


    public List<AdminEventDTO> getEventsByType(String eventType) {
        List<Event> events = eventRepository.findByEventTypeIgnoreCase(eventType);

        if (events.isEmpty()) {
            throw new ResourceNotFoundException("No events found for type: " + eventType);
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

    public void cancelEventByName(String eventName) {
        Event event = eventRepository.findByEventName(eventName)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with name: " + eventName));

        eventRepository.delete(event);
    }


    public EventDTO getEventByName(String eventName, String username) {
        Event event = eventRepository.findByEventName(eventName)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        boolean likedByUser = likeRepository.findByUserAndEvent(user, event).isPresent();
        int likesCount =(int) likeRepository.countByEvent(event);
        int commentsCount = commentRepository.findByEvent(event).size();

        return EventDTO.builder()
                .eventName(event.getEventName())
                .description(event.getDescription())
                .imageUrl(event.getImageUrl())
                .eventDateTime(event.getEventDateTime())
                .location(event.getLocation())
                .eventType(event.getEventType())
                .price(event.getPrice())
                .likesCount(likesCount)
                .commentsCount(commentsCount)
                .likedByUser(likedByUser)
                .build();
    }

    @Transactional
    public LikeResponseDTO toggleLike(String username, String eventName) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Event event = eventRepository.findByEventName(eventName)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        Like like = (Like) likeRepository.findByUserAndEvent(user, event).orElse(null);
        boolean liked;

        if (like != null) {
            likeRepository.delete(like);
            liked = false;
        } else {
            likeRepository.save(Like.builder().user(user).event(event).build());
            liked = true;
        }

        int likesCount = (int) likeRepository.countByEvent(event);

        return LikeResponseDTO.builder()
                .success(true)
                .liked(liked)
                .likesCount(likesCount)
                .build();
    }

    public List<CommentDTO> getComments(String eventName) {
        Event event = eventRepository.findByEventName(eventName)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        List<Comment> comments = commentRepository.findByEvent(event);

        return comments.stream()
                .map(c -> CommentDTO.builder()
                        .user(c.getUser().getUsername())
                        .content(c.getContent())
                        .build())
                .collect(Collectors.toList());
    }

    public CommentResponseDTO addComment(String username, String eventName, String content) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Event event = eventRepository.findByEventName(eventName)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        Comment comment = Comment.builder()
                .user(user)
                .event(event)
                .content(content)
                .build();
        commentRepository.save(comment);

        int commentsCount = commentRepository.findByEvent(event).size();

        return CommentResponseDTO.builder()
                .success(true)
                .commentsCount(commentsCount)
                .build();
    }

    public ResponseEntity<EventDetailAtBookDTO> getEventAvailability(String eventName) {
        Event event = eventRepository.findByEventName(eventName)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        EventDetailAtBookDTO dto = EventDetailAtBookDTO.builder()
                .location(event.getLocation())
                .price(event.getPrice())
                .availableTickets(event.getAvailableSeats())
                .build();

        return ResponseEntity.ok(dto);
    }
}
