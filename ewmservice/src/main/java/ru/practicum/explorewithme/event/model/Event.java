package ru.practicum.explorewithme.event.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.explorewithme.category.model.Category;
import ru.practicum.explorewithme.compilation.model.Compilation;
import ru.practicum.explorewithme.request.model.Request;
import ru.practicum.explorewithme.users.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "events")
@SecondaryTable(name = "locations", pkJoinColumns = @PrimaryKeyJoinColumn(name = "event_id"))
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

    @Column(name = "category_id")
    private Long categoryId;
    @Column(name = "initiator_id")
    private Long initiatorId;

    private LocalDateTime created;
    private LocalDateTime published;
    @Column(name = "event_date")
    private LocalDateTime eventDate;

    @Column(name = "participant_limit")
    private Integer participantLimit;
    private boolean paid;
    @Column(name = "request_moderation")
    private boolean requestModeration;

    @Enumerated(EnumType.STRING)
    private EventState state;

    @Embedded
    private Location location;

    @JoinColumn(name = "initiator_id", insertable = false, updatable = false, nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private User initiator;

    @JoinColumn(name = "category_id", insertable = false, updatable = false, nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private Category category;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "event_id")
    @ToString.Exclude
    private List<Request> requests;

    @ManyToMany(mappedBy = "events")
    @ToString.Exclude
    private Set<Compilation> compilations;

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
