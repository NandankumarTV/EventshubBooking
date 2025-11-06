package com.nandan.EventsHub.controller;

import com.nandan.EventsHub.dto.AdminEventDTO;
import com.nandan.EventsHub.dto.AdminLoginRequest;
import com.nandan.EventsHub.dto.EventRequestDTO;
import com.nandan.EventsHub.dto.LoginResponse;
import com.nandan.EventsHub.model.Event;
import com.nandan.EventsHub.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody AdminLoginRequest request) {
        LoginResponse response = adminService.authenticate(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/event/update")
    public ResponseEntity<Event> updateEvent(@RequestBody EventRequestDTO dto) {
        Event updatedEvent = adminService.updateEventByName(dto);
        return ResponseEntity.ok(updatedEvent);
    }
}

