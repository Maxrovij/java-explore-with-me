package ru.practicum.explorewithme.event.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class NewEventDto {

    @Length(min = 3, max = 120)
    private String title;

    @NotNull
    @Length(min = 20, max = 2000)
    private String annotation;

    @NotNull
    @Length(min = 20, max = 7000)
    private String description;

    @NotNull
    private Long category;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    @NotNull
    private Integer participantLimit = 0;

    private boolean paid;

    private boolean requestModeration = true;

    @NotNull
    private LocationDto location;
}