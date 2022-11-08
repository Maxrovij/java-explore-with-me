package ru.practicum.explorewithme.event.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.category.CategoryRepository;
import ru.practicum.explorewithme.event.EventMapper;
import ru.practicum.explorewithme.event.EventRepository;
import ru.practicum.explorewithme.event.model.*;
import ru.practicum.explorewithme.exceptions.EntityNotFoundException;
import ru.practicum.explorewithme.exceptions.OperationForbiddenException;
import ru.practicum.explorewithme.users.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PrivateEventService {

    private final EventMapper mapper;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final EventRepository repository;

    @Transactional
    public List<EventShortDto> getAllByUserId(Long userId, int from, int size) {
        List<Event> found = repository.findUserEvents(userId, from, size);
        return found.stream().map(mapper::toShortDto).collect(Collectors.toList());
    }

    public EventFullDto addNew(Long userId, NewEventDto eventDto) {
        if (!categoryRepository.existsById(eventDto.getCategory()))
            throw new EntityNotFoundException("No category with id " + eventDto.getCategory());

        if (!userRepository.existsById(userId))
            throw new EntityNotFoundException("User not found!");

        Event event = mapper.mapDtoToEvent(eventDto, userId);
        return mapper.toFullDto(repository.save(event));
    }

    @Transactional
    public EventFullDto patch(Long userId, UpdateEventRequest updateEventRequest) {
        Event event = mapper.mapPrivateUpdateRequestToEvent(userId, updateEventRequest);

        if (!event.getState().equals(EventState.PENDING))
            throw new OperationForbiddenException("Wrong event state!");

        return mapper.toFullDto(repository.save(event));
    }

    public EventFullDto getByEventId(Long userId, Long eventId) {
        Event event = checkIds(userId, eventId);
        return mapper.toFullDto(event);
    }

    @Transactional
    public EventFullDto cancel(Long userId, Long eventId) {
        Event event = checkIds(userId, eventId);
        if (event.getState().equals(EventState.CANCELED))
            throw new OperationForbiddenException("Wrong event state!");
        repository.cancelEvent(EventState.CANCELED, eventId);
        event.setState(EventState.CANCELED);
        return mapper.toFullDto(event);
    }

    private Event checkIds(Long userId, Long eventId) {
        if (!userRepository.existsById(userId))
            throw new EntityNotFoundException("No user found!");

        Optional<Event> maybeEvent = repository.findById(eventId);
        if (maybeEvent.isEmpty()) throw new EntityNotFoundException("Event not found!");

        Event event = maybeEvent.get();

        if (!event.getInitiatorId().equals(userId))
            throw new OperationForbiddenException("User is not initiator!");
        return event;
    }
}
