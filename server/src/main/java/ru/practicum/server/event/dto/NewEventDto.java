package ru.practicum.server.event.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.server.event.location.Location;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
public class NewEventDto {
    @Size(min = 20, max = 2000, message = "длин аннотации длжна быть min = 20, max = 2000")
    @NotNull(message = "анотация не должна быть null")
    private String annotation;
    @Min(1)
    @NotNull
    private Long category;
    @Size(min = 20, max = 7000, message = "длин описания длжна быть min = 20, max = 7000")
    @NotNull(message = "описание не должна быть null")
    private String description;
    @NotNull(message = "дата не должна быть null")
    private String eventDate;
    @NotNull(message = "локация не должна быть null")
    private Location location;
    private Boolean paid;
    @Min(0)
    private Integer participantLimit;
    private Boolean requestModeration;
    @Size(min = 3, max = 120, message = "длин заглавия длжна быть min = 3, max = 120")
    @NotNull(message = "заглавие не должна быть nulll")
    private String title;
}
