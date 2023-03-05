package ru.practicum.server.user.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.server.user.dto.UserDto;
import ru.practicum.server.user.dto.UserListDto;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userRequest);

    UserListDto getUsers(List<Long> ids, Pageable pageable);

    void deleteUser(Long userId);
}
