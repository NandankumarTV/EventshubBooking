package com.nandan.EventsHub.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCommentDTO {
    private String username;
    private String eventName;
    private String content;
}

