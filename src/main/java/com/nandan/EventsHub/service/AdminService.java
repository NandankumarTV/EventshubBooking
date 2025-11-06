
package com.nandan.EventsHub.service;

import com.nandan.EventsHub.dto.AdminEventDTO;
import com.nandan.EventsHub.dto.AdminLoginRequest;
import com.nandan.EventsHub.dto.EventRequestDTO;
import com.nandan.EventsHub.dto.LoginResponse;
import com.nandan.EventsHub.exception.ResourceNotFoundException;
import com.nandan.EventsHub.model.Admin;
import com.nandan.EventsHub.model.Event;
import com.nandan.EventsHub.repo.AdminRepository;
import com.nandan.EventsHub.repo.CommentRepository;
import com.nandan.EventsHub.repo.EventRepository;
import com.nandan.EventsHub.repo.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private AdminRepository adminRepository;

    public LoginResponse authenticate(AdminLoginRequest request) {
        Admin admin = adminRepository.findByAdminName(request.getAdminName())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid admin username or password"));

        if (admin.getPassword().equals(request.getPassword())) {
            return new LoginResponse( true,"✅ Login successful!");
        } else {
            return new LoginResponse(false, "❌ Invalid password!");
        }
    }

    public Event updateEventByName(EventRequestDTO dto) {
        Event event = eventRepository.findByEventName(dto.getEventName())
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with name: " + dto.getEventName()));

        event.setEventName(dto.getEventName());
        // Update fields
        event.setImageUrl(dto.getImageUrl());
        event.setLocation(dto.getLocation());
        event.setEventType(dto.getEventType());
        event.setEventDateTime(dto.getEventDateTime());
        event.setTotalSeats(dto.getTotalSeats());
        event.setAvailableSeats(dto.getAvailableSeats());
        event.setPrice(dto.getPrice());
        event.setDescription(dto.getDescription());

        // Save changes
        return eventRepository.save(event);
    }





}
