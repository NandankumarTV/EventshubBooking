package com.nandan.EventsHub.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventSummaryDTO {
    private Long eventId;
    private String eventName;
    private String imageUrl;
    private long likesCount;
    private long commentsCount;
    private boolean likedByUser; // true if the user liked this event
}

