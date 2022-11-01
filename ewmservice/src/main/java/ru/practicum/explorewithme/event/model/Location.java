package ru.practicum.explorewithme.event.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@Embeddable
@Table(name = "locations")
@NoArgsConstructor
@ToString
public class Location {

    @Column(name = "lat")
    private double lat;

    @Column(name = "lon")
    private double lon;
}
