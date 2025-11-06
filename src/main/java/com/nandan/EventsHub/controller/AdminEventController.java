package com.nandan.EventsHub.controller;

import com.nandan.EventsHub.dto.AdminEventDTO;
import com.nandan.EventsHub.dto.EventAvailabilityResponse;
import com.nandan.EventsHub.dto.EventCreateDTO;
import com.nandan.EventsHub.model.Event;
import com.nandan.EventsHub.service.AdminEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/event")
@CrossOrigin(origins = "*")
public class AdminEventController {

    @Autowired
    private AdminEventService adminEventService;

    @GetMapping("/check-availability")
    public ResponseEntity<EventAvailabilityResponse> checkAvailability(@RequestParam String eventName) {
        return ResponseEntity.ok(adminEventService.checkEventAvailability(eventName));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createEvent(@RequestBody EventCreateDTO dto) {
        Event saved = adminEventService.createEvent(dto);
        return ResponseEntity.ok("Event '" + saved.getEventName() + "' created successfully!");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteEvent(@RequestParam String eventName) {
        String message = adminEventService.cancelEventByName(eventName);
        return ResponseEntity.ok(message);
    }
    @GetMapping("/search")
    public ResponseEntity<List<AdminEventDTO>> searchEvents(@RequestParam String eventName) {
        return ResponseEntity.ok(adminEventService.searchEventsByName(eventName));
    }
}
