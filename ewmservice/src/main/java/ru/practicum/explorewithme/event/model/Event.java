package ru.practicum.explorewithme.event.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String annotation;
    private String description;

    private Long categoryId;
    private LocalDateTime created;

    @JoinColumn(name = "event_date")
    private LocalDateTime eventDate;

    @JoinColumn(name = "initiator_id")
    private Long initiatorId;
    private boolean paid;

    @JoinColumn(name = "participant_limit")
    private Integer participantLimit;
    private LocalDateTime published;

    @JoinColumn(name = "request_moderation")
    private boolean requestModeration;

    @Enumerated(EnumType.STRING)
    private EventState state;

    @Embedded
    private Location location;

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (this == o) return true;
        if (this.getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return id != null && Objects.equals(id, event.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

