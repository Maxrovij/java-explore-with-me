package ru.practicum.explorewithme.event.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.category.model.CategoryDto;
import ru.practicum.explorewithme.users.model.UserShortDto;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class EventShortDto {

    private Long id;
    private String title;
    private String annotation;
    private CategoryDto category;
    private Integer confirmedRequests;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private UserShortDto initiator;
    private boolean paid;
    private Integer views = 0;
}
