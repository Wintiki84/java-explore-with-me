package ru.practicum.server.comment.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Builder;
import lombok.Data;
import ru.practicum.server.comment.enums.CommentState;
import ru.practicum.validator.Details;
import ru.practicum.validator.Private;

@Data
@Builder
public class CommentShortDto {
    @JsonView({Private.class, Details.class})
    private String text;
    @JsonView({Private.class, Details.class})
    private String authorName;
    @JsonView({Private.class, Details.class})
    private CommentState state;
    @JsonView({Private.class, Details.class})
    private String created;
}
