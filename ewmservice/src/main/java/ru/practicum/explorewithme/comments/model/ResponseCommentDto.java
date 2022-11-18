package ru.practicum.explorewithme.comments.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ResponseCommentDto {
    private final Long id;
    private final Long userId;
    private final Long eventId;
    private final String content;
    private final LocalDateTime published;
    private final CommentState state;
}
