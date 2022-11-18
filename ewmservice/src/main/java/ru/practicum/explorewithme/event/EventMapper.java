package ru.practicum.explorewithme.event;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.category.CategoryRepository;
import ru.practicum.explorewithme.category.model.Category;
import ru.practicum.explorewithme.category.model.CategoryDto;
import ru.practicum.explorewithme.comments.CommentMapper;
import ru.practicum.explorewithme.comments.CommentRepository;
import ru.practicum.explorewithme.comments.model.Comment;
import ru.practicum.explorewithme.comments.model.CommentState;
import ru.practicum.explorewithme.event.model.*;
import ru.practicum.explorewithme.exceptions.EntityNotFoundException;
import ru.practicum.explorewithme.exceptions.OperationForbiddenException;
import ru.practicum.explorewithme.request.RequestRepository;
import ru.practicum.explorewithme.request.model.Request;
import ru.practicum.explorewithme.users.UserRepository;
import ru.practicum.explorewithme.users.model.User;
import ru.practicum.explorewithme.users.model.UserShortDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
@AllArgsConstructor
public class EventMapper {

    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    private final CommentRepository commentRepository;
    private final EventClient client;


    public EventShortDto toShortDto(Event event) {
        EventShortDto eventDto = new EventShortDto();

        eventDto.setId(event.getId());
        eventDto.setTitle(event.getTitle());
        eventDto.setAnnotation(event.getAnnotation());

        CategoryDto categoryDto = new CategoryDto();
        Category category = checkCategory(event.getCategoryId());
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        eventDto.setCategory(categoryDto);

        int confirmedRequests = requestRepository.getConfirmed(event.getId());
        eventDto.setConfirmedRequests(confirmedRequests);

        eventDto.setEventDate(event.getEventDate());

        UserShortDto userShortDto = new UserShortDto();
        User user = checkUser(event.getInitiatorId());
        userShortDto.setId(user.getId());
        userShortDto.setName(user.getName());
        eventDto.setInitiator(userShortDto);

        eventDto.setPaid(event.isPaid());

        String uri = "/events/" + event.getId();
        Integer views = (Integer) client.getViews(uri);
        eventDto.setViews(views);
        return eventDto;
    }

    public EventFullDto toFullDto(Event event) {
        EventFullDto eventDto = new EventFullDto();

        eventDto.setId(event.getId());
        eventDto.setTitle(event.getTitle());
        eventDto.setAnnotation(event.getAnnotation());
        eventDto.setDescription(event.getDescription());
        eventDto.setCreatedOn(event.getCreated());
        eventDto.setEventDate(event.getEventDate());

        LocationDto locationDto = new LocationDto();
        locationDto.setLat(event.getLocation().getLat());
        locationDto.setLon(event.getLocation().getLon());
        eventDto.setLocation(locationDto);

        int confirmedRequests = requestRepository.getConfirmed(event.getId());
        eventDto.setConfirmedRequests(confirmedRequests);

        CategoryDto categoryDto = new CategoryDto();
        Category category = checkCategory(event.getCategoryId());
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        eventDto.setCategory(categoryDto);

        UserShortDto userShortDto = new UserShortDto();
        User user = checkUser(event.getInitiatorId());
        userShortDto.setId(user.getId());
        userShortDto.setName(user.getName());
        eventDto.setInitiator(userShortDto);

        eventDto.setPaid(event.isPaid());
        eventDto.setParticipantLimit(event.getParticipantLimit());
        eventDto.setPublishedOn(event.getPublished());
        eventDto.setRequestModeration(event.isRequestModeration());
        eventDto.setState(event.getState());

        String uri = "/events/" + event.getId();
        Integer views = (Integer) client.getViews(uri);
        eventDto.setViews(views);

        List<Comment> comments = commentRepository.findAllByEventId(event.getId()).stream()
                .filter(comment -> (comment.getState() == CommentState.PUBLISHED)).collect(Collectors.toList());
        eventDto.setComments(CommentMapper.mapListToDto(comments));

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
        Event event = new Event();

        event.setTitle(eventDto.getTitle());
        event.setAnnotation(eventDto.getAnnotation());
        event.setDescription(eventDto.getDescription());
        event.setCategoryId(eventDto.getCategory());
        event.setCreated(LocalDateTime.now());
        event.setEventDate(eventDto.getEventDate());
        event.setInitiatorId(userId);
        event.setPaid(eventDto.isPaid());
        event.setParticipantLimit(eventDto.getParticipantLimit());
        event.setRequestModeration(eventDto.isRequestModeration());

        event.setState(EventState.PENDING);

        Location location = new Location();
        location.setLat(eventDto.getLocation().getLat());
        location.setLon(eventDto.getLocation().getLon());
        event.setLocation(location);

        return event;
    }

    public List<Event> mapEventIdsToEventList(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return List.of();
        return eventRepository.findAllById(ids);
    }

    public Event mapAdminRequestToEvent(Long eventId, AdminUpdateEventRequest eventRequest) {
        Event event = checkEvent(eventId);

        setEventProperties(
                event,
                eventRequest.getTitle(),
                eventRequest.getAnnotation(),
                eventRequest.getDescription(),
                eventRequest.getCategory(),
                eventRequest.getEventDate());

        if (eventRequest.getLocation() != null) {
            Location location = new Location();
            location.setLat(eventRequest.getLocation().getLat());
            location.setLon(eventRequest.getLocation().getLon());
            event.setLocation(location);
        }

        event.setPaid(eventRequest.isPaid());
        event.setParticipantLimit(eventRequest.getParticipantLimit());
        event.setRequestModeration(eventRequest.isRequestModeration());
        return event;
    }

    public Event mapPrivateUpdateRequestToEvent(Long userId, UpdateEventRequest request) {
        User user = checkUser(userId);
        Event event = checkEvent(request.getEventId());
        if (!event.getInitiatorId().equals(user.getId()))
            throw new OperationForbiddenException("Only initiator can update event!");

        setEventProperties(
                event,
                request.getTitle(),
                request.getAnnotation(),
                request.getDescription(),
                request.getCategory(),
                request.getEventDate());
        event.setPaid(request.isPaid());
        event.setParticipantLimit(request.getParticipantLimit());
        return event;
    }

    public List<Request> getRequestsByEventId(Long id) {
        return requestRepository.findAllByEventId(id);
    }

    private void setEventProperties(
            Event event,
            String title,
            String annotation,
            String description,
            long categoryId,
            LocalDateTime eventDate) {
        event.setTitle(title);
        event.setAnnotation(annotation);
        event.setDescription(description);

        checkCategory(categoryId);
        event.setCategoryId(categoryId);

        event.setEventDate(eventDate);
    }

    private Event checkEvent(Long id) {
        Optional<Event> maybeEvent = eventRepository.findById(id);
        if (maybeEvent.isEmpty()) throw new EntityNotFoundException("Event not found!");
        else return maybeEvent.get();
    }

    private Category checkCategory(Long id) {
        Optional<Category> maybeCategory = categoryRepository.findById(id);
        if (maybeCategory.isEmpty()) throw new EntityNotFoundException("Category not found");
        else return maybeCategory.get();
    }

    private User checkUser(Long id) {
        Optional<User> maybeUser = userRepository.findById(id);
        if (maybeUser.isEmpty()) throw new EntityNotFoundException("User not found");
        else return maybeUser.get();
    }
}
