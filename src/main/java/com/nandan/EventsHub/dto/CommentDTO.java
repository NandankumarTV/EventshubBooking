package com.nandan.EventsHub.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentDTO {
    private String user;
    private String content;
}