package ru.practicum.server.user.service;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.server.handler.exception.NotFoundException;
import ru.practicum.server.user.dto.UserBlockCommentStatusDto;
import ru.practicum.server.user.dto.UserDto;
import ru.practicum.server.user.dto.UserListDto;
import ru.practicum.server.user.enums.UserBanAction;
import ru.practicum.server.user.mapper.UserMapper;
import ru.practicum.server.user.model.QUser;
import ru.practicum.server.user.model.User;
import ru.practicum.server.user.repository.UserRepository;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService {
    private final UserRepository usersRepository;
    private final UserMapper mapper;

    @NotNull
    @Override
    @Transactional
    public UserDto createUser(@NotNull UserDto userDto) {
        userDto.setCommentsAreProhibited(false);
        return mapper.mapToUserDto(usersRepository.save(mapper.mapToUser(userDto)));
    }

    @NotNull
    @Override
    @Transactional(readOnly = true)
    public UserListDto getUsers(List<Long> ids, Pageable pageable) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (ids != null && !ids.isEmpty()) {
            booleanBuilder.and(QUser.user.id.in(ids));
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
    @Transactional
    public void deleteUser(@NotNull Long userId) {
        if (usersRepository.existsById(userId)) {
            usersRepository.deleteById(userId);
        } else {
            throw new NotFoundException("Пользователь с id=" + userId + " не найден");
        }
    }

    @NotNull
    @Override
    @Transactional
    public UserListDto changeUserCommentsStatus(@NotNull UserBlockCommentStatusDto users) {
        List<UserDto> response = usersRepository.findAllByIdIn(users.getUserIds()).stream().peek(u -> {
            if (users.getStatus().equals(UserBanAction.BANNED)) {
                u.setCommentsAreProhibited(Boolean.TRUE);
            }
            if (users.getStatus().equals(UserBanAction.UNBANNED)) {
                u.setCommentsAreProhibited(Boolean.FALSE);
            }
        }).map(mapper::mapToUserDto).collect(Collectors.toList());
        return UserListDto
                .builder()
                .users(response)
                .build();
    }
}
