
package com.nandan.EventsHub.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventDetailAtBookDTO {
    private String location;
    private double price;
    private int availableTickets;
}
