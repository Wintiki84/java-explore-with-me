package ru.practicum.server.user.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.server.user.dto.UserBlockCommentStatusDto;
import ru.practicum.server.user.dto.UserDto;
import ru.practicum.server.user.dto.UserListDto;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface UserService {
    @NotNull
    UserDto createUser(@NotNull UserDto userRequest);

    @NotNull
    UserListDto getUsers(List<Long> ids, Pageable pageable);

    void deleteUser(@NotNull Long userId);

    @NotNull
    UserListDto changeUserCommentsStatus(@NotNull UserBlockCommentStatusDto users);
}
