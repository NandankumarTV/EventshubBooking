package com.nandan.EventsHub.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingResponseAllDTO {

    private String ticketId;
    private String eventName;
    private String location;
    private LocalDateTime eventDateTime;
    private int numberOfTickets;
    private double totalAmount;
    private LocalDateTime bookingTime;
}
