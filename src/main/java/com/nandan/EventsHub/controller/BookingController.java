package com.nandan.EventsHub.controller;

import com.nandan.EventsHub.dto.ApiResponse;
import com.nandan.EventsHub.dto.BookingRequestDTO;
import com.nandan.EventsHub.dto.BookingResponseAllDTO;
import com.nandan.EventsHub.dto.BookingResponseDTO;
import com.nandan.EventsHub.model.*;
import com.nandan.EventsHub.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/events/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping("/add")
    public ResponseEntity<BookingResponseDTO> addBooking(@RequestBody BookingRequestDTO request) {
        BookingResponseDTO response = bookingService.createBooking(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping("/user/{username}")
    public ResponseEntity<List<BookingResponseAllDTO>> getUserBookings(@PathVariable String username) {
        List<BookingResponseAllDTO> bookings = bookingService.getBookingsByUser(username);
        return ResponseEntity.ok(bookings);
    }

    @DeleteMapping("/cancel/{ticketId}")
    public ResponseEntity<ApiResponse> cancelBooking(@PathVariable String ticketId) {
        ApiResponse response = bookingService.cancelBooking(ticketId);
        return ResponseEntity.ok(response);
    }

}
