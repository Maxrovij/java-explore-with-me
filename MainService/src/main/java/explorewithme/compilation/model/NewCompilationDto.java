package explorewithme.compilation.model;

import explorewithme.event.model.EventShortDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
public class NewCompilationDto {
    private Long id;
    @NotNull
    @Length(min = 5, max = 150, message = "Title length must be from 5 to 150 characters")
    private String title;
    private boolean pinned;
    private List<Long> events;
}

