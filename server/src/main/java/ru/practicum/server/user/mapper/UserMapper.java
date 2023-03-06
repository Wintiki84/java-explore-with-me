package ru.practicum.server.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import ru.practicum.server.user.dto.UserDto;
import ru.practicum.server.user.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User mapToUser(UserDto userDto);

    @Mapping(source = "userId", target = "id")
    UserDto mapToUserDto(User user);

    List<UserDto> mapToUserDto(Page<User> page);
}
