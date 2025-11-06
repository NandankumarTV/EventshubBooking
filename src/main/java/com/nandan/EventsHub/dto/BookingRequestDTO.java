
package com.nandan.EventsHub.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingRequestDTO {
    private String username;       // who is booking
    private String eventName;      // event being booked
    private int numberOfTickets;   // how many tickets
    private double totalAmount;    // total price calculated on frontend
}
