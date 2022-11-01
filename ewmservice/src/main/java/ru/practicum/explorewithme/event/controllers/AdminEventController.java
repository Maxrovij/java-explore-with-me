package ru.practicum.explorewithme.event.controllers;

import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.event.model.AdminUpdateEventRequest;
import ru.practicum.explorewithme.event.model.EventFullDto;
import ru.practicum.explorewithme.event.model.EventState;
import ru.practicum.explorewithme.event.services.AdminEventService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/events")
@AllArgsConstructor
public class AdminEventController {

    private final AdminEventService service;

    @PutMapping("/{eventId}")
    public EventFullDto putEvent(@RequestBody AdminUpdateEventRequest request,
                                 @PathVariable Long eventId) {
        return service.putEvent(eventId, request);
    }

    @PatchMapping("/{eventId}/publish")
    public EventFullDto publish(@PathVariable Long eventId) {
        return service.setState(eventId, true);
    }

    @PatchMapping("{eventId}/reject")
    public EventFullDto reject(@PathVariable Long eventId) {
        return service.setState(eventId, false);
    }

    @GetMapping
    public List<EventFullDto> getByParams(@RequestParam(required = false) Long[] userIds,
                                          @RequestParam(required = false) EventState[] states,
                                          @RequestParam(required = false) Long[] categoryIds,
                                          @RequestParam(required = false)
                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime rangeStart,
                                          @RequestParam(required = false)
                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime rangeEnd,
                                          @RequestParam(defaultValue = "0") int from,
                                          @RequestParam(defaultValue = "10") int size) {
        return service.getByParams(userIds, states, categoryIds, rangeStart, rangeEnd, from, size);
    }
}
