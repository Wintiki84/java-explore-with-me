package ru.practicum.server.user.controller;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.user.dto.UserBlockCommentStatusDto;
import ru.practicum.server.user.dto.UserDto;
import ru.practicum.server.user.dto.UserListDto;
import ru.practicum.server.user.service.UserService;
import ru.practicum.validator.AdminDetails;
import ru.practicum.validator.Create;
import ru.practicum.validator.Update;

import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/admin/users")
@Slf4j
@Validated
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserControllerAdmin {
    private final UserService userService;

    @JsonView(AdminDetails.class)
    @PostMapping
    public ResponseEntity<UserDto> createUser(@Validated(Create.class) @RequestBody UserDto userDto) {
        log.info("создание пользователя: {}", userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userDto));
    }

    @JsonView(AdminDetails.class)
    @GetMapping
    public ResponseEntity<UserListDto> getUsers(@RequestParam(required = false) List<Long> ids,
                                                @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                                @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Получить пользователей из списков: ids: {}, from: {},size: {}", ids, from, size);
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUsers(ids, PageRequest.of(from / size, size)));
    }

    @JsonView(AdminDetails.class)
    @DeleteMapping("{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable @Positive Long userId) {
        log.info("удалить пользователя с id={}", userId);
        userService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @JsonView(AdminDetails.class)
    @PatchMapping("comments")
    public ResponseEntity<UserListDto> changeCommentBlockedStatus(
            @Validated(Update.class) @RequestBody UserBlockCommentStatusDto users) {
        log.info("изменение статуса блокировки пользователей:{}", users);
        return ResponseEntity.status(HttpStatus.OK).body(userService.changeUserCommentsStatus(users));
    }
}
