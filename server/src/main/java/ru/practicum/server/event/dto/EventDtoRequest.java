package ru.practicum.server.event.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Builder;
import lombok.Data;
import ru.practicum.server.event.location.Location;
import ru.practicum.validator.*;

import javax.validation.constraints.*;

@Data
@Builder
public class EventDtoRequest {
    @JsonView({AdminDetails.class, Private.class})
    @Null(groups = {Create.class, Update.class}, message = "Должно быть пустым")
    @Positive(message = "Должно быть больше нуля")
    private Long id;
    @JsonView({AdminDetails.class, Private.class})
    @Size(groups = {Create.class, Update.class}, min = 20, max = 2000,
            message = "длинна аннотации длжна быть min = 20, max = 2000")
    @NotNull(groups = {Create.class}, message = "анотация не должна быть null")
    private String annotation;
    @JsonView({AdminDetails.class, Private.class})
    @Positive(groups = {Create.class, Update.class}, message = "должно быть положительным")
    @NotNull(groups = {Create.class}, message = "катгория не должна быть null")
    private Long category;
    @JsonView({AdminDetails.class, Private.class})
    @Size(groups = {Create.class, Update.class}, min = 20, max = 7000,
            message = "длина описания длжна быть min = 20, max = 7000")
    @NotNull(groups = {Create.class}, message = "описание не должна быть null")
    private String description;
    @JsonView({AdminDetails.class, Private.class})
    @NotNull(groups = {Create.class}, message = "дата не должна быть null")
    private String eventDate;
    @JsonView({AdminDetails.class, Private.class})
    @NotNull(groups = {Create.class}, message = "локация не должна быть null")
    private Location location;
    @JsonView({AdminDetails.class, Private.class})
    private Boolean paid;
    @JsonView({AdminDetails.class, Private.class})
    @Min(0)
    private Integer participantLimit;
    @JsonView({AdminDetails.class, Private.class})
    private Boolean requestModeration;
    @JsonView({AdminDetails.class, Private.class})
    private String stateAction;
    @JsonView({AdminDetails.class, Private.class})
    @Size(groups = {Create.class, Update.class}, min = 3, max = 120,
            message = "длина заглавия должна быть min = 3, max = 120")
    @NotNull(groups = {Create.class}, message = "заглавие не должна быть null")
    private String title;
}
