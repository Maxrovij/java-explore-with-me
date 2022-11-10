package ru.practicum.explorewithme.comments.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.comments.model.NewCommentDto;
import ru.practicum.explorewithme.comments.model.ResponseCommentDto;
import ru.practicum.explorewithme.comments.services.PrivateCommentService;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/users/{userId}")
@AllArgsConstructor
@Slf4j
public class PrivateCommentController {

    private final PrivateCommentService service;

    @PostMapping("/events/{eventId}/comments")
    public ResponseCommentDto postNew(@PathVariable @NotNull Long userId,
                                      @PathVariable @NotNull Long eventId,
                                      @RequestBody NewCommentDto commentDto) {
        log.info("Endpoint POST /users/{userId}/events/{eventId}/comments. \n " +
                        "userId={} \n " +
                        "eventId={} \n" +
                        "comment content: {}",
                userId, eventId, commentDto.getContent());

        return service.postNew(commentDto, userId, eventId);
    }

    @DeleteMapping("/comments/{commentId}")
    public void delete(@PathVariable @NotNull Long userId,
                       @PathVariable @NotNull Long commentId) {
        log.info("Endpoint DELETE /users/{userId}/comments/{commentId}. \n " +
                        "userId={} \n " +
                        "commentId={}",
                userId, commentId);

        service.delete(userId, commentId);
    }

    @PatchMapping("/comments/{commentId}")
    public ResponseCommentDto edit(@PathVariable @NotNull Long userId,
                                   @PathVariable @NotNull Long commentId,
                                   @RequestBody NewCommentDto commentDto) {
        log.info("Endpoint PATCH /users/{userId}/comments/{commentId}. \n " +
                        "userId={} \n " +
                        "commentId={} \n " +
                        "new content: {}",
                userId, commentId, commentDto.getContent());

        return service.edit(commentDto, userId, commentId);
    }
}

