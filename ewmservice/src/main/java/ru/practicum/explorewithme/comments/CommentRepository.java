package ru.practicum.explorewithme.comments;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.comments.model.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(value = "select * from COMMENTS where STATE=?1", nativeQuery = true)
    List<Comment> getByState(String state);

    List<Comment> findAllByEventId(Long id);
}
