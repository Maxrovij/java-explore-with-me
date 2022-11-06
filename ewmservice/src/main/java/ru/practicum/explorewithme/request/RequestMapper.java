package ru.practicum.explorewithme.request;

import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.request.model.ParticipantRequestDto;
import ru.practicum.explorewithme.request.model.Request;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RequestMapper {

    public ParticipantRequestDto mapRequestToParticipantRequestDto(Request request) {
        ParticipantRequestDto requestDto = new ParticipantRequestDto();
        requestDto.setId(request.getId());
        requestDto.setEvent(request.getEventId());
        requestDto.setCreated(request.getCreated());
        requestDto.setRequester(request.getRequesterId());
        requestDto.setStatus(request.getStatus());
        return requestDto;
    }

    public List<ParticipantRequestDto> mapList(List<Request> list) {
        return list.stream()
                .map(this::mapRequestToParticipantRequestDto)
                .collect(Collectors.toList());
    }
}
