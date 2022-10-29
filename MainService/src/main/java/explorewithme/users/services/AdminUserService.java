package explorewithme.users.services;

import explorewithme.exception.EntityAlreadyExistsException;
import explorewithme.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import explorewithme.users.UserMapper;
import explorewithme.users.UserRepository;
import explorewithme.users.model.User;
import explorewithme.users.model.UserFullDto;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AdminUserService {

    private final UserRepository repository;

    public List<UserFullDto> getUsers(Long[] ids, int from, int size) {
        List<Long> idList = Arrays.asList(ids);
        List<User> foundUsers = repository.findAllByIds(idList, from, size);
        return foundUsers.stream().map(UserMapper::toFullDto).collect(Collectors.toList());
    }

    public UserFullDto add(UserFullDto userDto) {
        try {
            User user = repository.save(new User(null, userDto.getEmail(), userDto.getName()));
            return UserMapper.toFullDto(user);
        } catch (DuplicateKeyException e) {
            throw new EntityAlreadyExistsException(
                    String.format("User with email %s already exists", userDto.getEmail()));
        }
    }

    public void delete(Long userId) {
        if (repository.existsById(userId)) repository.deleteById(userId);
        else throw new EntityNotFoundException(String.format("User with id %d not found", userId));
    }
}
