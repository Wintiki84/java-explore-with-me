package ru.practicum.server.comment.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Builder;
import lombok.Data;
import ru.practicum.server.comment.enums.CommentState;
import ru.practicum.validator.*;
import ru.practicum.validator.Short;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
public class CommentDto {
    @JsonView({Private.class, Details.class, AdminDetails.class})
    @Null(groups = {Create.class, Update.class}, message = "Должно быть пустым")
    private Long id;
    @JsonView({Private.class, Details.class, AdminDetails.class, Short.class})
    @Size(groups = {Create.class, Update.class}, min = 1, max = 7000, message = "Недопустимый комментарий")
    @NotNull(groups = {Create.class, Update.class}, message = "коментарий не должна быть null")
    private String text;
    @JsonView({Private.class, Details.class, AdminDetails.class, Short.class})
    @Null(groups = {Create.class, Update.class}, message = "Должно быть пустым")
    private CommentState state;
    @JsonView({Private.class, Details.class, AdminDetails.class, Short.class})
    @Null(groups = {Create.class, Update.class}, message = "Должно быть пустым")
    private LocalDateTime created;
    @JsonView({Private.class, Details.class, AdminDetails.class, Short.class})
    @Null(groups = {Create.class, Update.class}, message = "Должно быть пустым")
    private String authorName;
    @JsonView({Private.class, Details.class, AdminDetails.class})
    @Null(groups = {Create.class, Update.class}, message = "Должно быть пустым")
    private String eventTitle;
}
