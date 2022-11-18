package ru.practicum.explorewithme.comments;

import ru.practicum.explorewithme.comments.model.Comment;
import ru.practicum.explorewithme.comments.model.ResponseCommentDto;

import java.util.List;
import java.util.stream.Collectors;

public class CommentMapper {

    public static List<ResponseCommentDto> mapListToDto(List<Comment> comments) {
        return comments.stream().map(CommentMapper::mapToDto).collect(Collectors.toList());
    }

    public static ResponseCommentDto mapToDto(Comment comment) {
        return ResponseCommentDto.builder()
                .id(comment.getId())
                .userId(comment.getUserId())
                .eventId(comment.getEventId())
                .content(comment.getContent())
                .published(comment.getPublished() == null ? null : comment.getPublished())
                .state(comment.getState())
                .build();
    }
}
