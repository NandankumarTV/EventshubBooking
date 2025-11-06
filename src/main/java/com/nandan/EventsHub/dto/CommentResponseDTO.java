package com.nandan.EventsHub.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentResponseDTO {
    private boolean success;
    private int commentsCount;
}