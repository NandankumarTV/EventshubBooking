package com.nandan.EventsHub.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminEventDTO {
    private String imageUrl;
    private String eventName;
    private String location;
    private long likesCount;
    private long commentsCount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Kolkata")
    private LocalDateTime localDateTime;
}
