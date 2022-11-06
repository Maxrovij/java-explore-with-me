package ru.practicum.explorewithme.event.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
@ToString
public class Location {

    private double lat;
    private double lon;
}
