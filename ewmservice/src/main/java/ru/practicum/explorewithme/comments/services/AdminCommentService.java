package ru.practicum.explorewithme.comments.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.comments.CommentMapper;
import ru.practicum.explorewithme.comments.CommentRepository;
import ru.practicum.explorewithme.comments.model.Comment;
import ru.practicum.explorewithme.comments.model.CommentState;
import ru.practicum.explorewithme.comments.model.ResponseCommentDto;
import ru.practicum.explorewithme.exceptions.EntityNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AdminCommentService {

    private final CommentRepository repository;

    public ResponseCommentDto setState(Long commentId, boolean published) {
        Optional<Comment> maybeComment = repository.findById(commentId);
        if (maybeComment.isEmpty()) throw new EntityNotFoundException("Comment not found!");

        Comment comment = maybeComment.get();

        if (published) {
            comment.setState(CommentState.PUBLISHED);
            comment.setPublished(LocalDateTime.now());
        } else {
            comment.setState(CommentState.REJECTED);
        }
        return CommentMapper.mapToDto(repository.save(comment));
    }

    public List<ResponseCommentDto> getByState(String state) {
        return CommentMapper.mapListToDto(repository.findAllByState(CommentState.valueOf(state)));
    }
}

