package com.nandan.EventsHub.dto;
 import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikeToggleResponseDTO {
    private boolean liked; // true if liked after operation
    private long likesCount;
}
