package ru.practicum.explorewithme.comments;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.comments.model.Comment;
import ru.practicum.explorewithme.comments.model.CommentState;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByState(CommentState state);

    List<Comment> findAllByEventId(Long id);
}
