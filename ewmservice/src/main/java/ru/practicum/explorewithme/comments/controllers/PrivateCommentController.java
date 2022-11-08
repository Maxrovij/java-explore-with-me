package ru.practicum.explorewithme.comments.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.comments.model.NewCommentDto;
import ru.practicum.explorewithme.comments.model.ResponseCommentDto;
import ru.practicum.explorewithme.comments.services.PrivateCommentService;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/users/{userId}")
@AllArgsConstructor
public class PrivateCommentController {

    private final PrivateCommentService service;

    @PostMapping("/events/{eventId}/comments")
    public ResponseCommentDto postNew(@PathVariable @NotNull Long userId,
                                      @PathVariable @NotNull Long eventId,
                                      @RequestBody NewCommentDto commentDto) {
        return service.postNew(commentDto, userId, eventId);
    }

    @DeleteMapping("/comments/{commentId}")
    public void delete(@PathVariable @NotNull Long userId,
                       @PathVariable @NotNull Long commentId) {
        service.delete(userId, commentId);
    }

    @PatchMapping("/comments/{commentId}")
    public ResponseCommentDto edit(@PathVariable @NotNull Long userId,
                                   @PathVariable @NotNull Long commentId,
                                   @RequestBody NewCommentDto commentDto) {
        return service.edit(commentDto, userId, commentId);
    }
}

