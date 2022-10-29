package explorewithme.event.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
@ToString
public class Location {

    @Column(name = "lat", table = "locations")
    private double lat;

    @Column(name = "lon", table = "locations")
    private double lon;
}
