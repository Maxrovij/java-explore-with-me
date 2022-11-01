package ru.practicum.explorewithme.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.event.model.EventState;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    @Query("select e from Event as e where e.paid=?1 and e.state=?2 and e.eventDate > ?3 order by e.eventDate")
    List<Event> findAllByParams(boolean paid, EventState state, LocalDateTime start);

    @Query(value = "select * from events where initiator_id=?1 limit ?3 offset ?2", nativeQuery = true)
    List<Event> findUserEvents(Long userId, int from, int size);

    @Modifying
    @Query("update Event e set e.state = ?1 where e.id = ?2")
    void cancelEvent(EventState state, Long eventId);

    Event findByInitiatorId(Long id);
}
