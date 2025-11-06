package com.nandan.EventsHub.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikeActionResponseDTO {
    private String message;
    private boolean liked; // true if now liked, false if now unliked
}
