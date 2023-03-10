package ru.practicum.server.comment.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Builder;
import lombok.Getter;
import ru.practicum.validator.Details;
import ru.practicum.validator.Private;
import ru.practicum.validator.Short;

import java.util.List;

@Builder
@Getter
public class CommentDtoList {
    @JsonValue
    @JsonView({Private.class, Details.class, Short.class})
    private List<CommentShortDto> comments;
}
