package com.nandan.EventsHub.controller;

import com.nandan.EventsHub.dto.*;
import com.nandan.EventsHub.model.Event;
import com.nandan.EventsHub.model.User;
import com.nandan.EventsHub.service.CommentService;
import com.nandan.EventsHub.service.EventService;
import com.nandan.EventsHub.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private CommentService commentService;

    @PostMapping("/create")
    public ResponseEntity<Event> createEvent(@RequestBody EventRequestDTO dto) {
        Event event = eventService.createEvent(dto);
        return new ResponseEntity<>(event, HttpStatus.CREATED);
    }

    @GetMapping("/check-name")
    public ResponseEntity<Map<String, Object>> checkEventNameAvailability(@RequestParam String eventName) {
        return eventService.isEventNameAvailable(eventName);
    }

    @DeleteMapping("/cancel/{eventName}")
    public ResponseEntity<?> cancelEvent(@PathVariable String eventName) {
        eventService.cancelEventByName(eventName);
        return ResponseEntity.ok(Map.of(
                "eventName", eventName,
                "message", "Event cancelled successfully"
        ));
    }

    @GetMapping("/type/{eventType}")
    public ResponseEntity<List<AdminEventDTO>> getEventsByType(@PathVariable String eventType) {
        List<AdminEventDTO> events = eventService.getEventsByType(eventType);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/names/{eventName}")
    public ResponseEntity<EventDTO> getEvent(
            @PathVariable String eventName,
            @RequestParam String username) {
        EventDTO event = eventService.getEventByName(eventName, username);
        return ResponseEntity.ok(event); // 200 OK
    }

    // POST toggle like
    @PostMapping("/likes/toggle")
    public ResponseEntity<LikeResponseDTO> toggleLike(@RequestBody Map<String,String> request) {
        String username = request.get("username");
        String eventName = request.get("eventName");
        LikeResponseDTO response = eventService.toggleLike(username, eventName);
        return ResponseEntity.ok(response);
    }

    // GET comments
    @GetMapping("/comments/event/{eventName}")
    public ResponseEntity<List<CommentDTO>> getComments(@PathVariable String eventName) {
        List<CommentDTO> comments = eventService.getComments(eventName);
        return ResponseEntity.ok(comments);
    }

    // POST add comment
    @PostMapping("/comments/add")
    public ResponseEntity<CommentResponseDTO> addComment(@RequestBody Map<String,String> request) {
        String username = request.get("username");
        String eventName = request.get("eventName");
        String content = request.get("content");
        CommentResponseDTO response = eventService.addComment(username, eventName, content);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/name/{eventName}")
    public ResponseEntity<EventDetailAtBookDTO> getEventDetails(@PathVariable String eventName) {
        return eventService.getEventAvailability(eventName);
    }





}
