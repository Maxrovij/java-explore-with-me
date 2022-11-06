package ru.practicum.explorewithme.request.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.request.model.ParticipantRequestDto;
import ru.practicum.explorewithme.request.services.PrivateRequestService;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}")
@AllArgsConstructor
public class PrivateRequestController {

    private final PrivateRequestService service;

    @GetMapping("/requests")
    public List<ParticipantRequestDto> getUserRequests(@PathVariable Long userId) {
        return service.getUserRequests(userId);
    }

    @PostMapping("/requests")
    public ParticipantRequestDto post(@PathVariable Long userId,
                                      @RequestParam Long eventId) {
        return service.postRequest(userId, eventId);
    }

    @PatchMapping("/requests/{reqId}/cancel")
    public ParticipantRequestDto cancel(@PathVariable Long userId,
                                        @PathVariable Long reqId) {
        return service.cancel(userId, reqId);
    }

    @GetMapping("/events/{eventId}/requests")
    public List<ParticipantRequestDto> getByEventId(@PathVariable Long userId,
                                                    @PathVariable Long eventId) {
        return service.getByEvent(userId, eventId);
    }

    @PatchMapping("/events/{eventId}/requests/{reqId}/confirm")
    public ParticipantRequestDto confirmByEventId(@PathVariable Long userId,
                                                  @PathVariable Long eventId,
                                                  @PathVariable Long reqId) {
        return service.setRequestStatus(userId, eventId, reqId, true);
    }

    @PatchMapping("/events/{eventId}/requests/{reqId}/reject")
    public ParticipantRequestDto rejectByEventId(@PathVariable Long userId,
                                                 @PathVariable Long eventId,
                                                 @PathVariable Long reqId) {
        return service.setRequestStatus(userId, eventId, reqId, false);
    }
}
