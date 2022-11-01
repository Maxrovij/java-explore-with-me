package ru.practicum.explorewithme.category.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class CategoryDto {
    private Long id;
    @NotNull(message = "Name can not be null")
    private String name;
}
