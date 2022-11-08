package ru.practicum.explorewithme.comments.services;

import lombok.AllArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.comments.CommentMapper;
import ru.practicum.explorewithme.comments.CommentRepository;
import ru.practicum.explorewithme.comments.model.Comment;
import ru.practicum.explorewithme.comments.model.CommentState;
import ru.practicum.explorewithme.comments.model.NewCommentDto;
import ru.practicum.explorewithme.comments.model.ResponseCommentDto;
import ru.practicum.explorewithme.event.EventRepository;
import ru.practicum.explorewithme.exceptions.EntityNotFoundException;
import ru.practicum.explorewithme.exceptions.OperationForbiddenException;
import ru.practicum.explorewithme.users.UserRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
public class PrivateCommentService {

    private final CommentRepository repository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public ResponseCommentDto postNew(NewCommentDto newCommentDto, Long userId, Long eventId) {
        checkUserId(userId);
        checkEventId(eventId);
        try {
            Comment comment = new Comment();
            comment.setUserId(userId);
            comment.setEventId(eventId);
            comment.setContent(newCommentDto.getContent());
            comment.setState(CommentState.PENDING);
            return CommentMapper.mapToDto(repository.save(comment));
        } catch (DuplicateKeyException e) {
            throw new OperationForbiddenException("Content must be unique!");
        }
    }

    public void delete(Long userId, Long commentId) {
        checkUserId(userId);
        Comment comment = checkAuthor(userId, commentId);
        repository.delete(comment);
    }

    public ResponseCommentDto edit(NewCommentDto newCommentDto, Long userId, Long commentId) {
        checkUserId(userId);
        Comment comment = checkAuthor(userId, commentId);
        try {
            if (comment.getState() == CommentState.PENDING) {
                comment.setContent(newCommentDto.getContent());
                return CommentMapper.mapToDto(repository.save(comment));
            } else {
                comment.setContent(newCommentDto.getContent());
                comment.setState(CommentState.PENDING);
                comment.setPublished(null);
                return CommentMapper.mapToDto(repository.save(comment));
            }
        } catch (DuplicateKeyException e) {
            throw new OperationForbiddenException("Content must be unique!");
        }
    }

    private void checkUserId(Long id) {
        if (!userRepository.existsById(id)) throw new EntityNotFoundException("User not found!");
    }

    private Comment checkAuthor(Long userId, Long commentId) {
        Comment comment = checkCommentId(commentId);
        if (!userId.equals(comment.getUserId()))
            throw new OperationForbiddenException("Only author can delete comment!");
        else return comment;
    }

    private void checkEventId(Long id) {
        if (!eventRepository.existsById(id)) throw new EntityNotFoundException("Event not found!");
    }

    private Comment checkCommentId(Long id) {
        Optional<Comment> maybeComment = repository.findById(id);
        if (maybeComment.isEmpty()) throw new EntityNotFoundException("Comment not found!");
        else return maybeComment.get();
    }
}

