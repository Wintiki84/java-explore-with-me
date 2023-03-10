package ru.practicum.server.user.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Builder;
import lombok.Data;
import ru.practicum.server.user.enums.UserBanAction;
import ru.practicum.validator.AdminDetails;
import ru.practicum.validator.Update;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
public class UserBlockCommentStatusDto {
    @JsonView({AdminDetails.class})
    @NotNull(groups = {Update.class}, message = "Не должно быть Null")
    private List<Long> userIds;
    @JsonView({AdminDetails.class})
    @NotNull(groups = {Update.class}, message = "Не должно быть Null")
    private UserBanAction status;
}