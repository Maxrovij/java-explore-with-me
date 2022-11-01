package ru.practicum.explorewithme.event.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.event.EventMapper;
import ru.practicum.explorewithme.event.EventRepository;
import ru.practicum.explorewithme.event.model.AdminUpdateEventRequest;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.event.model.EventFullDto;
import ru.practicum.explorewithme.event.model.EventState;
import ru.practicum.explorewithme.exception.EntityNotFoundException;
import ru.practicum.explorewithme.exception.OperationForbiddenException;
import ru.practicum.explorewithme.tools.EventFilter;
import ru.practicum.explorewithme.tools.PageCreator;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class AdminEventService {

    private final EventRepository repository;
    private final EventMapper mapper;
    private final PageCreator<Event> pageCreator;

    public EventFullDto putEvent(Long eventId, AdminUpdateEventRequest request) {
        Event event = checkId(eventId);
        mapper.map(request, event);
        repository.save(event);
        return mapper.toFullDto(event);
    }

    @Transactional
    public EventFullDto setState(Long eventId, boolean approved) {
        Event event = checkId(eventId);
        if (!event.getState().equals(EventState.PENDING))
            throw new OperationForbiddenException("Event is not pending!");

        if (approved) {
            event.setState(EventState.PUBLISHED);
            event.setCreated(LocalDateTime.now());
        } else {
            event.setState(EventState.REJECTED);
        }
        return mapper.toFullDto(repository.save(event));
    }

    public List<EventFullDto> getByParams(Long[] userIds,
                                          EventState[] states,
                                          Long[] categories,
                                          LocalDateTime rangeStart,
                                          LocalDateTime rangeEnd,
                                          int from,
                                          int size) {
        List<Event> events = Stream.of(userIds)
                .map(repository::findByInitiatorId)
                .filter(Objects::nonNull)
                .map(event -> EventFilter.filterByCatId(event, List.of(categories)))
                .map(event -> EventFilter.filterByStates(event, List.of(states)))
                .map(event -> EventFilter.filterByEndDate(event, rangeEnd))
                .map(event -> EventFilter.filterByStartDate(event, rangeStart))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return pageCreator
                .getPage(events, from, size)
                .getContent()
                .stream()
                .map(mapper::toFullDto)
                .collect(Collectors.toList());
    }

    private Event checkId(Long eventId) {
        Optional<Event> maybeEvent = repository.findById(eventId);
        if (maybeEvent.isEmpty())
            throw new EntityNotFoundException("Event not found!");
        return maybeEvent.get();
    }
}
