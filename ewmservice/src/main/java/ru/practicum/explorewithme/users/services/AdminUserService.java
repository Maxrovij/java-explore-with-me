package ru.practicum.explorewithme.users.services;

import lombok.AllArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.exception.EntityAlreadyExistsException;
import ru.practicum.explorewithme.exception.EntityNotFoundException;
import ru.practicum.explorewithme.users.UserRepository;
import ru.practicum.explorewithme.users.model.User;
import ru.practicum.explorewithme.users.model.UserFullDto;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;

    public List<UserFullDto> getUsers(Long[] ids, int from, int size) {
        List<Long> idList = Arrays.asList(ids);
        List<User> foundUsers = userRepository.findAllByIds(idList, from, size);
        return foundUsers.stream().map(this::toFullDto).collect(Collectors.toList());
    }

    public UserFullDto add(UserFullDto userDto) {
        try {
            User user = new User();
            user.setName(userDto.getName());
            user.setEmail(userDto.getEmail());

            return toFullDto(userRepository.save(user));
        } catch (DuplicateKeyException e) {
            throw new EntityAlreadyExistsException(
                    String.format("User with email %s already exists", userDto.getEmail()));
        }
    }

    public void delete(Long userId) {
        if (userRepository.existsById(userId)) userRepository.deleteById(userId);
        else throw new EntityNotFoundException(String.format("User with id %d not found", userId));
    }

    private UserFullDto toFullDto(User u) {
        UserFullDto dto = new UserFullDto();
        dto.setId(u.getId());
        dto.setEmail(u.getEmail());
        dto.setName(u.getName());
        return dto;
    }
}
