
// LikeResponseDTO.java
package com.nandan.EventsHub.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LikeResponseDTO {
    private boolean success;
    private boolean liked;
    private int likesCount;
}