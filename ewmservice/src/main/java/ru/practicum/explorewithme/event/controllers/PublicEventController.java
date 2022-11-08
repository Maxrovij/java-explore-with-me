package ru.practicum.explorewithme.event.controllers;

import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.event.EventClient;
import ru.practicum.explorewithme.event.model.EventFullDto;
import ru.practicum.explorewithme.event.model.EventShortDto;
import ru.practicum.explorewithme.event.model.EventSortType;
import ru.practicum.explorewithme.event.services.PublicEventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/events")
@AllArgsConstructor
public class PublicEventController {

    private final PublicEventService service;
    private final EventClient client;

    @GetMapping
    public List<EventShortDto> getAll(@RequestParam(required = false) String text,
                                      @RequestParam(required = false) Long[] categories,
                                      @RequestParam(defaultValue = "false") boolean paid,
                                      @RequestParam(required = false)
                                      @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                      @RequestParam(required = false)
                                      @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                      @RequestParam(defaultValue = "true") boolean onlyAvailable,
                                      @RequestParam(defaultValue = "EVENT_DATE") EventSortType sortType,
                                      @Valid @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                      @Valid @Positive @RequestParam(defaultValue = "10") int size,
                                      HttpServletRequest httpServletRequest) {
        client.addRequest(httpServletRequest);
        return service.getByParams(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sortType, from, size);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getById(@PathVariable Long eventId, HttpServletRequest httpServletRequest) {
        client.addRequest(httpServletRequest);
        return service.getById(eventId);
    }
}
