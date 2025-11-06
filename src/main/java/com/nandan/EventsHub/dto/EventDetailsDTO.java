package com.nandan.EventsHub.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventDetailsDTO {
    private String imageUrl;
    private String eventName;
    private String location;
    private String description;
    private String eventType;
    private double price;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Kolkata")
    private LocalDateTime eventDateTime;

    private long likesCount;
    private long commentsCount;
}
