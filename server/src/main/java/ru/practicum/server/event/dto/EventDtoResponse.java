package ru.practicum.server.event.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Builder;
import lombok.Data;
import ru.practicum.server.category.dto.CategoryDto;
import ru.practicum.server.comment.dto.CommentShortDto;
import ru.practicum.server.event.enums.State;
import ru.practicum.server.event.location.Location;
import ru.practicum.server.user.dto.UserDto;
import ru.practicum.validator.*;

import javax.validation.constraints.*;
import java.util.Set;


@Data
@Builder
public class EventDtoResponse {
    @JsonView({Details.class, AdminDetails.class, Private.class})
    private Long id;
    @JsonView({Details.class, AdminDetails.class, Private.class})
    private String annotation;
    @JsonView({Details.class, AdminDetails.class, Private.class})
    private CategoryDto category;
    @JsonView({Details.class, AdminDetails.class, Private.class})
    private Integer confirmedRequests;
    @JsonView({Details.class, AdminDetails.class, Private.class})
    private String createdOn;
    @JsonView({Details.class, AdminDetails.class, Private.class})
    private String description;
    @JsonView({Details.class, AdminDetails.class, Private.class})
    private String eventDate;
    @JsonView({Details.class, AdminDetails.class, Private.class})
    private Location location;
    @JsonView({Details.class, AdminDetails.class, Private.class})
    private UserDto initiator;
    @JsonView({Details.class, AdminDetails.class, Private.class})
    private Boolean paid;
    @JsonView({Details.class, AdminDetails.class, Private.class})
    private Integer participantLimit;
    @JsonView({Details.class, AdminDetails.class, Private.class})
    private String publishedOn;
    @JsonView({Details.class, AdminDetails.class, Private.class})
    private Boolean requestModeration;
    @JsonView({Details.class, AdminDetails.class, Private.class})
    private State state;
    @JsonView({Details.class, AdminDetails.class, Private.class})
    private String title;
    @JsonView({Details.class, AdminDetails.class, Private.class})
    private Long views;
    @JsonView({Details.class, AdminDetails.class, Private.class})
    private Set<CommentShortDto> comments;
}
