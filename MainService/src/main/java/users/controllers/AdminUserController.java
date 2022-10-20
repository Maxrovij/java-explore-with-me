package users.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import users.model.UserFullDto;
import users.services.AdminUserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;
import java.util.List;

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
    public void addNew() {

    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable Long userId) {

    }
}
