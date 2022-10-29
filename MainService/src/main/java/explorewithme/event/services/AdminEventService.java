package explorewithme.event.services;

import explorewithme.event.EventMapper;
import explorewithme.event.EventRepository;
import explorewithme.event.model.AdminUpdateEventRequest;
import explorewithme.event.model.Event;
import explorewithme.event.model.EventFullDto;
import explorewithme.event.model.EventState;
import explorewithme.exception.EntityNotFoundException;
import explorewithme.exception.OperationForbiddenException;
import explorewithme.tools.EventFilter;
import explorewithme.tools.PageCreator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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
