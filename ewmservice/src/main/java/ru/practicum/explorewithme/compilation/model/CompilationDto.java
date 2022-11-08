package ru.practicum.explorewithme.compilation.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.event.model.EventShortDto;

import java.util.List;

@Data
@NoArgsConstructor
public class CompilationDto {

    private Long id;
    private String title;
    private boolean pinned;
    private List<EventShortDto> events;
}
