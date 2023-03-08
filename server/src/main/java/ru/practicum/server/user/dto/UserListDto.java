package ru.practicum.server.user.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Builder;
import lombok.Getter;
import ru.practicum.validator.AdminDetails;

import java.util.List;

@Builder
@Getter
public class UserListDto {
    @JsonView({AdminDetails.class})
    @JsonValue
    private List<UserDto> users;
}
