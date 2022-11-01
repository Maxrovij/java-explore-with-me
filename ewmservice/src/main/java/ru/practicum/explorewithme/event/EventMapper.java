package ru.practicum.explorewithme.event;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.event.model.*;
import ru.practicum.explorewithme.request.RequestRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class EventMapper extends ModelMapper {

    private final RequestRepository repository;
    private final EventClient client;

    public EventMapper(RequestRepository requestRepo, EventClient eventClient) {
        this.repository = requestRepo;
        this.client = eventClient;
        setUp();
    }

    public EventShortDto toShortDto(Event event) {
        EventShortDto eventDto = super.map(event, EventShortDto.class);
        int confirmedRequests = repository.getConfirmed(event.getId());
        eventDto.setConfirmedRequests(confirmedRequests);
        String uri = "/events/" + event.getId();
        Integer views = (Integer) client.getViews(uri);
        eventDto.setViews(views);
        return eventDto;
    }

    public EventFullDto toFullDto(Event event) {
        EventFullDto eventDto = super.map(event, EventFullDto.class);
        int confirmedRequests = repository.getConfirmed(event.getId());
        eventDto.setConfirmedRequests(confirmedRequests);
        String uri = "/events/" + event.getId();
        Integer views = (Integer) client.getViews(uri);
        eventDto.setViews(views);
        return eventDto;
    }

    public List<EventShortDto> getSorted(List<Event> list, EventSortType sortType) {
        return list.stream()
                .map(this::toShortDto)
                .sorted((e1, e2) -> {
                    if (sortType.equals(EventSortType.VIEWS)) {
                        return e1.getViews().compareTo(e2.getViews());
                    } else {
                        return e1.getEventDate().compareTo(e2.getEventDate());
                    }
                })
                .collect(Collectors.toList());
    }

    public Event mapDtoToEvent(NewEventDto eventDto, long userId) {
        Event event = super.map(eventDto, Event.class);
        event.setCategoryId(eventDto.getCategory());
        event.setInitiatorId(userId);
        event.setCreated(LocalDateTime.now());
        event.setState(EventState.PENDING);
        return event;
    }

    public void map(UpdateEventRequest eventRequest, Event event) {
        super.map(eventRequest, event);
        event.setId(eventRequest.getEventId());
        event.setCategoryId(eventRequest.getCategory());
    }

    private void setUp() {
        this.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }
}
