package ru.practicum.server.report.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Builder;
import lombok.Data;
import ru.practicum.validator.AdminDetails;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@Builder
public class ReportDto {
    @JsonView(AdminDetails.class)
    @NotNull(message = "Не должно быть null")
    @Positive(message = "Должно быть больше нуля")
    Long id;
    @JsonView(AdminDetails.class)
    @NotNull(message = "Не должно быть null")
    @Positive(message = "Должно быть больше нуля")
    Long reportedUser;
    @JsonView(AdminDetails.class)
    @NotNull(message = "Не должно быть null")
    @NotBlank(message = "Не должно быть пустым")
    String reportedMessage;
}
