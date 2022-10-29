package explorewithme.users.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import explorewithme.users.model.UserFullDto;
import explorewithme.users.services.AdminUserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@RestController
@RequestMapping("/admin/users")
@AllArgsConstructor
public class AdminUserController {

    private final AdminUserService service;

    @GetMapping
    public Collection<UserFullDto> getUsers(@RequestParam Long[] ids,
                                            @Valid @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                            @Valid @Positive @RequestParam(defaultValue = "10") int size) {
        return service.getUsers(ids, from, size);
    }

    @PostMapping
    public UserFullDto addNew(@RequestBody UserFullDto userDto) {
        return service.add(userDto);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable Long userId) {
        service.delete(userId);
    }
}
