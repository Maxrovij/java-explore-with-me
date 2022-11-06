package ru.practicum.explorewithme.stats;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ViewStats {

    private final String app;
    private final String uri;
    private final Long hits;
}
