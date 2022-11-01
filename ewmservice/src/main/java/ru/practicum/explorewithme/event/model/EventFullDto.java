package ru.practicum.explorewithme.event.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.category.model.CategoryDto;
import ru.practicum.explorewithme.users.model.UserShortDto;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class EventFullDto {

    private Long id;
    private String annotation;
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private LocationDto location;
    private Integer confirmedRequests;
    private CategoryDto category;
    private UserShortDto initiator;
    private boolean paid;
    private Integer participantLimit;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;

    private boolean requestModeration;
    private EventState state;
    private String title;
    private int views;
}
