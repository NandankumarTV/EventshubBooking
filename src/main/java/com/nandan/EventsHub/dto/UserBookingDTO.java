package com.nandan.EventsHub.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserBookingDTO {
    private String eventName;
    private String location;
    private LocalDateTime eventDateTime;
    private double price;        // Assuming Event has a price field
    private int numberOfTickets; // Count of bookings for this event
    private double totalAmount;
    private String ticketId;
}

