package ru.practicum.explorewithme.request.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.event.EventRepository;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.event.model.EventState;
import ru.practicum.explorewithme.exception.EntityNotFoundException;
import ru.practicum.explorewithme.exception.OperationForbiddenException;
import ru.practicum.explorewithme.request.RequestMapper;
import ru.practicum.explorewithme.request.RequestRepository;
import ru.practicum.explorewithme.request.model.ParticipantRequestDto;
import ru.practicum.explorewithme.request.model.Request;
import ru.practicum.explorewithme.request.model.RequestStatus;
import ru.practicum.explorewithme.users.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PrivateRequestService {

    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final RequestRepository repository;
    private final RequestMapper mapper;

    public List<ParticipantRequestDto> getUserRequests(Long userId) {
        List<Request> requests = repository.findAllByRequesterId(checkUserId(userId));
        return mapper.mapList(requests);
    }

    public ParticipantRequestDto postRequest(Long userId, Long eventId) {
        checkUserId(userId);
        Event event = getEventById(eventId);

        if (event.getInitiatorId().equals(userId))
            throw new OperationForbiddenException("You can't send request on your own event, you stupid!!!");

        if (!event.getState().equals(EventState.PUBLISHED))
            throw new OperationForbiddenException("Event is not published yet!");

        int eventConfirmedRequests = requestRepository.getConfirmed(eventId);
        int limit = event.getParticipantLimit();
        if (eventConfirmedRequests >= limit)
            throw new OperationForbiddenException("Too many participants!");

        Request request;
        if (event.isRequestModeration()) {
            request = repository.save(new Request(userId, eventId, RequestStatus.PENDING));
        } else {
            request = repository.save(new Request(userId, eventId, RequestStatus.CONFIRMED));
        }
        return mapper.map(request);
    }

    @Transactional
    public ParticipantRequestDto cancel(Long userId, Long requestId) {
        checkUserId(userId);
        Request request = getRequestById(requestId);
        if (request.getStatus().equals(RequestStatus.CANCELED))
            throw new OperationForbiddenException("Request was canceled earlier!");
        if (!request.getRequesterId().equals(userId))
            throw new OperationForbiddenException("You are not requester!");
        request.setStatus(RequestStatus.CANCELED);
        return mapper.map(repository.save(request));
    }

    public List<ParticipantRequestDto> getByEvent(Long userId, Long eventId) {
        checkUserId(userId);
        Event event = getEventById(eventId);
        if (!event.getInitiatorId().equals(userId))
            throw new OperationForbiddenException("User is not initiator!");

        return mapper.mapList(event.getRequests());
    }

    public ParticipantRequestDto setRequestStatus(Long userId, Long eventId, Long requestId, boolean approved) {
        checkUserId(userId);
        List<Request> requests = getEventById(eventId).getRequests();
        Request request = getRequestById(requestId);
        if (!requests.contains(request))
            throw new OperationForbiddenException("Wrong request");

        if (request.getStatus().equals(RequestStatus.PENDING)) {
            if (approved) {
                request.setStatus(RequestStatus.CONFIRMED);
            } else {
                request.setStatus(RequestStatus.REJECTED);
            }
        } else {
            throw new OperationForbiddenException("Request is not in Pending status");
        }
        return mapper.map(repository.save(request));
    }

    private Request getRequestById(long reqId) {
        Optional<Request> optionalRequest = repository.findById(reqId);
        if (optionalRequest.isEmpty()) {
            throw new EntityNotFoundException("Request not found!");
        }
        return optionalRequest.get();
    }

    private Long checkUserId(Long userId) {
        if (!userRepository.existsById(userId))
            throw new EntityNotFoundException("User not found");
        else return userId;
    }

    private Event getEventById(Long eventId) {
        Optional<Event> mayBeEvent = eventRepository.findById(eventId);
        if (mayBeEvent.isEmpty())
            throw new EntityNotFoundException("Event not found!");
        else return mayBeEvent.get();
    }
}
