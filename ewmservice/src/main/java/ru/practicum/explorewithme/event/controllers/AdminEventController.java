package ru.practicum.explorewithme.event.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
    public List<EventFullDto> getByParams(@RequestParam(required = false) Long[] users,
                                          @RequestParam(required = false) EventState[] states,
                                          @RequestParam(required = false) Long[] categories,
                                          @RequestParam(required = false)
                                          @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                          @RequestParam(required = false)
                                          @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                          @RequestParam(defaultValue = "0") int from,
                                          @RequestParam(defaultValue = "10") int size) {
        log.info("GET admin/events \n " +
                "PARAMS: \n " +
                "users: {} \n " +
                "states: {} \n " +
                "categories: {} \n " +
                "rangeStart: {} \n " +
                "rangeEnd: {} \n " +
                "from: {} \n " +
                "size: {}", users, states, categories, rangeStart, rangeEnd, from, size);

        return service.getByParams(users, states, categories, rangeStart, rangeEnd, from, size);
    }
}
