package com.nandan.EventsHub.service;

import com.nandan.EventsHub.dto.ApiResponse;
import com.nandan.EventsHub.dto.BookingRequestDTO;
import com.nandan.EventsHub.dto.BookingResponseAllDTO;
import com.nandan.EventsHub.dto.BookingResponseDTO;
import com.nandan.EventsHub.exception.BadRequestException;
import com.nandan.EventsHub.exception.ResourceNotFoundException;
import com.nandan.EventsHub.model.Booking;
import com.nandan.EventsHub.model.Event;
import com.nandan.EventsHub.model.User;
import com.nandan.EventsHub.repo.BookingRepository;
import com.nandan.EventsHub.repo.EventRepository;
import com.nandan.EventsHub.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private BookingRepository bookingRepository;


    @Transactional
    public BookingResponseDTO createBooking(BookingRequestDTO request) {

        if (request.getNumberOfTickets() <= 0) {
            throw new BadRequestException("Number of tickets must be greater than 0");
        }

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.getUsername()));

        Event event = eventRepository.findByEventName(request.getEventName())
                .orElseThrow(() -> new ResourceNotFoundException("Event not found: " + request.getEventName()));

        if (request.getNumberOfTickets() > event.getAvailableSeats()) {
            throw new BadRequestException("Not enough tickets available for this event");
        }

        // ✅ Deduct available seats
        int remaining = event.getAvailableSeats() - request.getNumberOfTickets();
        event.setAvailableSeats(remaining);
        eventRepository.save(event);

        // ✅ Create booking
        Booking booking = Booking.builder()
                .ticketId(UUID.randomUUID().toString())
                .user(user)
                .event(event)
                .bookingTime(LocalDateTime.now())
                .numberOfTickets(request.getNumberOfTickets())
                .totalAmount(request.getTotalAmount())
                .location(event.getLocation())
                .build();

     //   System.out.println("Booking successfully done");

        bookingRepository.save(booking);

        // ✅ Return response DTO
        return BookingResponseDTO.builder()
                .ticketId(booking.getTicketId())
                .username(user.getUsername())
                .eventName(event.getEventName())
                .location(event.getLocation())
                .numberOfTickets(booking.getNumberOfTickets())
                .totalAmount(booking.getTotalAmount())
                .bookingTime(booking.getBookingTime())
                .build();
    }


    public List<BookingResponseAllDTO> getBookingsByUser(String username) {
        List<Booking> bookings = bookingRepository.findByUserUsernameOrderByEventEventDateTimeDesc(username);

        return bookings.stream()
                .map(b -> BookingResponseAllDTO.builder()
                        .ticketId(b.getTicketId())
                        .eventName(b.getEvent().getEventName())
                        .location(b.getLocation())
                        .eventDateTime(b.getEvent().getEventDateTime())
                        .numberOfTickets(b.getNumberOfTickets())
                        .totalAmount(b.getTotalAmount())
                        .bookingTime(b.getBookingTime())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public ApiResponse cancelBooking(String ticketId) {

        // 1️⃣ Find booking
        Booking booking = bookingRepository.findByTicketId(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found for ticket ID: " + ticketId));

        // 2️⃣ Extract event and ticket count from booking
        Event event = booking.getEvent();
        int ticketsToRestore = booking.getNumberOfTickets();

        // 3️⃣ Restore event availability
        event.setAvailableSeats(event.getAvailableSeats() + ticketsToRestore);
        eventRepository.save(event);

        // 4️⃣ Delete booking
        bookingRepository.delete(booking);

        // 5️⃣ Return success
        return new ApiResponse(true,
                "Booking cancelled successfully. " + ticketsToRestore + " seats restored for event: " + event.getEventName());
    }

}
