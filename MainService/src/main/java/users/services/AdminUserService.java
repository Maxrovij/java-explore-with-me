package users.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import users.UserMapper;
import users.UserRepository;
import users.model.User;
import users.model.UserFullDto;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AdminUserService {

    private final UserRepository repository;

    public List<UserFullDto> getUsers(Long[] ids, int from, int size) {
        List<Long> idList = Arrays.asList(ids);
        List<User> foundUsers = repository.findAllByIdWithPagination(idList);
        return foundUsers.stream().map(UserMapper::toFullDto).collect(Collectors.toList());
    }
}
