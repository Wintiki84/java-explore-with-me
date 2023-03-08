package ru.practicum.server.event.location;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.validator.AdminDetails;
import ru.practicum.validator.Private;

import javax.persistence.Embeddable;


@Getter
@Setter
@Embeddable
public class Location {
    @JsonView({AdminDetails.class, Private.class})
    private Float lat;
    @JsonView({AdminDetails.class, Private.class})
    private Float lon;
}
