package ru.practicum.explorewithme.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.users.model.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "select * from users where id in ?1 limit ?3 offset ?2", nativeQuery = true)
    List<User> findAllByIds(List<Long> ids, int from, int size);
}
