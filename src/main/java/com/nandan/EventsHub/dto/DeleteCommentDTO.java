package com.nandan.EventsHub.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeleteCommentDTO {
    private String username;
    private Long commentId;
}

