package explorewithme.event.controllers;

import explorewithme.event.EventClient;
import explorewithme.event.model.EventFullDto;
import explorewithme.event.model.EventShortDto;
import explorewithme.event.model.EventSortType;
import explorewithme.event.services.PublicEventService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

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
                                      @RequestParam(required = false) Long[] categoryIds,
                                      @RequestParam(defaultValue = "false") boolean paid,
                                      @RequestParam(required = false)
                                      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime rangeStart,
                                      @RequestParam(required = false)
                                      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime rangeEnd,
                                      @RequestParam(defaultValue = "true") boolean onlyAvailable,
                                      @RequestParam(defaultValue = "EVENT_DATE") EventSortType sortType,
                                      @Valid @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                      @Valid @Positive @RequestParam(defaultValue = "10") int size,
                                      HttpServletRequest httpServletRequest) {
        client.addRequest(httpServletRequest);
        return service.getByParams(text, categoryIds, paid, rangeStart, rangeEnd, onlyAvailable, sortType, from, size);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getById(@PathVariable Long eventId, HttpServletRequest httpServletRequest) {
        client.addRequest(httpServletRequest);
        return service.getById(eventId);
    }
}