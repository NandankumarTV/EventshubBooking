package com.nandan.EventsHub.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class EventDTO {
    private String eventName;
    private String description;
    private String imageUrl;
    private LocalDateTime eventDateTime;
    private String location;
    private String eventType;
    private double price;
    private int likesCount;
    private int commentsCount;
    private boolean likedByUser;
}
