package explorewithme.event.controllers;

import explorewithme.event.model.*;
import explorewithme.event.services.PrivateEventService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}")
@AllArgsConstructor
public class PrivateEventController {

    private final PrivateEventService service;

    @GetMapping("/events")
    public List<EventShortDto> getAllByUserId(@PathVariable Long userId,
                                              @Valid @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                              @Valid @Positive @RequestParam(defaultValue = "10") int size) {
        return service.getAllByUserId(userId, from, size);
    }

    @PostMapping("/events")
    public EventFullDto addNew(@PathVariable Long userId,
                               @RequestBody @Valid NewEventDto eventDto) {
        return service.addNew(userId, eventDto);
    }

    @PatchMapping("/events")
    public EventFullDto editEvent(@PathVariable Long userId,
                                  @Valid @RequestBody UpdateEventRequest updateEventRequest) {
        return service.patch(userId, updateEventRequest);
    }

    @GetMapping("/events/{eventId}")
    public EventFullDto getByEventId(@PathVariable Long userId,
                                     @PathVariable Long eventId) {
        return service.getByEventId(userId, eventId);
    }

    @PatchMapping("/events/{eventId}")
    public EventFullDto canselEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        return service.cancel(userId, eventId);
    }
}
