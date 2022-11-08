package ru.practicum.explorewithme.comments.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class NewCommentDto {
    @NotBlank
    @NotNull
    @Length(min = 10, max = 2000, message = "Comment length must be more than 10 and less than 2000 characters!")
    private String content;
}
