package ru.practicum.server.request.controller;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.request.dto.RequestDto;
import ru.practicum.server.request.dto.RequestListDto;
import ru.practicum.server.request.service.RequestService;
import ru.practicum.validator.Private;

import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/users")
@Validated
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PrivateControllerRequest {
    private final RequestService requestService;

    @JsonView(Private.class)
    @PostMapping("{userId}/requests")
    public ResponseEntity<RequestDto> createRequest(@PathVariable @Positive Long userId,
                                                    @RequestParam @Positive Long eventId) {
        log.info("создание запроса с userId={} и eventId={}", userId, eventId);
        return ResponseEntity.status(HttpStatus.CREATED).body(requestService.createRequest(userId, eventId));
    }

    @JsonView(Private.class)
    @GetMapping("{userId}/requests")
    public ResponseEntity<RequestListDto> getUserRequests(@PathVariable @Positive Long userId) {
        log.info("получение запроса с userId={}", userId);
        return ResponseEntity.status(HttpStatus.OK).body(requestService.getUserRequests(userId));
    }

    @JsonView(Private.class)
    @PatchMapping("{userId}/requests/{requestId}/cancel")
    public ResponseEntity<RequestDto> cancelRequest(@PathVariable @Positive Long userId,
                                                                 @PathVariable @Positive Long requestId) {
        log.info("отмена запроса с id={} и userId={}", requestId, userId);
        return ResponseEntity.status(HttpStatus.OK).body(requestService.canceledRequest(userId, requestId));
    }
}
