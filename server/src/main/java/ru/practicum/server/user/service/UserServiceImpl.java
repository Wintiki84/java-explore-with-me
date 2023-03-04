package ru.practicum.server.user.service;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.server.handler.exception.NotFoundException;
import ru.practicum.server.user.dto.UserDto;
import ru.practicum.server.user.dto.UserListDto;
import ru.practicum.server.user.mapper.UserMapper;
import ru.practicum.server.user.model.QUser;
import ru.practicum.server.user.model.User;
import ru.practicum.server.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService {
    private final UserRepository usersRepository;
    private final UserMapper mapper;

    @Override
    public UserDto createUser(UserDto userDto) {
        return mapper.mapToUserDto(usersRepository.save(mapper.mapToUser(userDto)));
    }

    @Override
    public UserListDto getUsers(List<Long> ids, Pageable pageable) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (ids != null && !ids.isEmpty()) {
            booleanBuilder.and(QUser.user.userId.in(ids));
        }
        Page<User> page;
        if (booleanBuilder.getValue() != null) {
            page = usersRepository.findAll(booleanBuilder.getValue(), pageable);
        } else {
            page = usersRepository.findAll(pageable);
        }
        return UserListDto
                .builder()
                .users(mapper.mapToUserDto(page))
                .build();
    }

    @Override
    public void deleteUser(Long userId) {
        if (usersRepository.existsById(userId)) {
            usersRepository.deleteById(userId);
        } else {
            throw new NotFoundException("Пользователь с id=" + userId + " не найден");
        }
    }
}
