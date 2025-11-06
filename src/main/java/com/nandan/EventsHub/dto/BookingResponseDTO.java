package com.nandan.EventsHub.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingResponseDTO {
    private String ticketId;
    private String username;
    private String eventName;
    private String location;
    private int numberOfTickets;
    private double totalAmount;
    private LocalDateTime bookingTime;
}
