package explorewithme.users.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class UserFullDto {
    private Long id;

    @Email
    @NotNull
    @NotBlank
    private String email;

    @NotBlank
    @NotNull
    private String name;
}
