package explorewithme.event.services;

import explorewithme.event.EventMapper;
import explorewithme.event.EventRepository;
import explorewithme.event.model.*;
import explorewithme.exception.EntityNotFoundException;
import explorewithme.tools.EventFilter;
import explorewithme.tools.PageCreator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PublicEventService {

    private final PageCreator<EventShortDto> pageCreator;

    private final EventMapper mapper;
    private final EventRepository repository;

    public List<EventShortDto> getByParams(
            String text,
            Long[] categoryIds,
            boolean paid,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            boolean onlyAvailable,
            EventSortType sortType,
            int from,
            int size) {
        LocalDateTime start = rangeStart == null ? LocalDateTime.now() : rangeStart;
        List<Event> found = repository.findAllByParams(paid, EventState.PUBLISHED, start)
                .stream()
                .map(event -> EventFilter.filterByEndDate(event, rangeEnd))
                .map(event -> EventFilter.filterByAvailable(event, onlyAvailable))
                .map(event -> EventFilter.filterByCatId(event, Arrays.asList(categoryIds)))
                .map(event -> EventFilter.filterByText(event, text))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        List<EventShortDto> sorted = mapper.getSorted(found, sortType);
        if (sorted != null) return pageCreator.getPage(sorted, from, size).getContent();
        else return List.of();
    }

    public EventFullDto getById(Long eventId) {
        Optional<Event> maybeEvent = repository.findById(eventId);
        if (maybeEvent.isEmpty())
            throw new EntityNotFoundException(String.format("Event with id %d not found!", eventId));
        return mapper.toFullDto(maybeEvent.get());
    }


}
