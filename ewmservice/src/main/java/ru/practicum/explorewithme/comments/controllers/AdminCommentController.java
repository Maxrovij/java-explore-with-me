package ru.practicum.explorewithme.comments.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.comments.model.ResponseCommentDto;
import ru.practicum.explorewithme.comments.services.AdminCommentService;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/admin/comments")
@AllArgsConstructor
public class AdminCommentController {

    private final AdminCommentService service;

    @PatchMapping("/{commentId}/publish")
    public ResponseCommentDto publish(@PathVariable @NotNull Long commentId) {
        return service.setState(commentId, true);
    }

    @PatchMapping("/{commentId}/reject")
    public ResponseCommentDto reject(@PathVariable @NotNull Long commentId) {
        return service.setState(commentId, false);
    }

    @GetMapping
    public List<ResponseCommentDto> getByState(@RequestParam String state) {
        return service.getByState(state);
    }
}

