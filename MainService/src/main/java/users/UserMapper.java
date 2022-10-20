package users;

import users.model.User;
import users.model.UserFullDto;

public class UserMapper {
    public static UserFullDto toFullDto(User u) {
        UserFullDto dto = new UserFullDto();
        dto.setId(u.getId());
        dto.setEmail(u.getEmail());
        dto.setName(u.getName());
        return dto;
    }
}
