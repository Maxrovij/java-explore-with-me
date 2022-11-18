package ru.practicum.explorewithme.event.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.event.EventMapper;
import ru.practicum.explorewithme.event.EventRepository;
import ru.practicum.explorewithme.event.model.AdminUpdateEventRequest;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.event.model.EventFullDto;
import ru.practicum.explorewithme.event.model.EventState;
import ru.practicum.explorewithme.exceptions.EntityNotFoundException;
import ru.practicum.explorewithme.exceptions.OperationForbiddenException;
import ru.practicum.explorewithme.tools.EventFilter;
import ru.practicum.explorewithme.tools.PageCreator;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AdminEventService {

    private final EventRepository repository;
    private final EventMapper mapper;
    private final PageCreator<Event> pageCreator;

    public EventFullDto putEvent(Long eventId, AdminUpdateEventRequest request) {
        Event event = mapper.mapAdminRequestToEvent(eventId, request);
        return mapper.toFullDto(repository.save(event));
    }

    @Transactional
    public EventFullDto setState(Long eventId, boolean approved) {
        Event event = checkId(eventId);
        if (!event.getState().equals(EventState.PENDING))
            throw new OperationForbiddenException("Event is not pending!");

        if (approved) {
            event.setState(EventState.PUBLISHED);
            event.setPublished(LocalDateTime.now());
        } else {
            event.setState(EventState.CANCELED);
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
        List<Event> events = repository.findAllByInitiatorIdList(List.of(userIds)).stream()
                .map(event -> EventFilter.filterByCatId(event, categories))
                .filter(Objects::nonNull)
                .map(event -> EventFilter.filterByStates(event, states))
                .filter(Objects::nonNull)
                .map(event -> EventFilter.filterByEndDate(event, rangeEnd))
                .filter(Objects::nonNull)
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
