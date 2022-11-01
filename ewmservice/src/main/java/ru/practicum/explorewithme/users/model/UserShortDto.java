package ru.practicum.explorewithme.users.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserShortDto {
    private Long id;
    private String name;
}
