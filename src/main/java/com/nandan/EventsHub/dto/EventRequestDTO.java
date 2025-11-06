package com.nandan.EventsHub.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventRequestDTO {
    private String eventName;
    private String imageUrl;
    private String location;
    private String eventType;
    private LocalDateTime eventDateTime;
    private int totalSeats;
    private int availableSeats;
    private double price;
    private String description;
}
