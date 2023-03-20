package ru.practicum.server.event.controller;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.event.dto.EventDtoRequest;
import ru.practicum.server.event.dto.EventDtoResponse;
import ru.practicum.server.event.dto.ListEventShortDto;
import ru.practicum.server.event.service.EventService;
import ru.practicum.server.request.dto.EventRequestStatusUpdate;
import ru.practicum.server.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.server.request.dto.RequestListDto;
import ru.practicum.validator.Create;
import ru.practicum.validator.Private;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/users")
@Validated
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PrivateEventController {
    private final EventService eventService;

    @JsonView({Private.class})
    @PostMapping("{userId}/events")
    public ResponseEntity<EventDtoResponse> addEvent(@PathVariable @Positive Long userId,
                                                     @RequestBody @Validated(Create.class) EventDtoRequest eventDto) {
        log.info("добавить новое событие ползователя с id={}: {}", userId, eventDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.addEvent(userId, eventDto));
    }

    @JsonView({Private.class})
    @GetMapping("{userId}/events")
    public ResponseEntity<ListEventShortDto> getUserEvents(@RequestParam(defaultValue = "0") @Min(0) Integer from,
                                                           @RequestParam(defaultValue = "10") @Positive Integer size,
                                                           @PathVariable @Positive Long userId) {
        log.info("получить события userId={}, from: {},size: {}", userId, from, size);
        return ResponseEntity.status(HttpStatus.OK)
                .body(eventService.getPrivateUserEvents(userId, PageRequest.of(from / size, size)));
    }

    @JsonView({Private.class})
    @GetMapping("{userId}/events/{eventId}")
    public ResponseEntity<EventDtoResponse> getUserEvent(@PathVariable @Positive Long userId,
                                                     @PathVariable @Positive Long eventId) {
        log.info("получить события с eventId={} и userId={}", eventId, userId);
        return ResponseEntity.status(HttpStatus.OK).body(eventService.getPrivateUserEvent(userId, eventId));
    }

    @JsonView({Private.class})
    @PatchMapping("{userId}/events/{eventId}")
    public ResponseEntity<EventDtoResponse> updateEvent(@PathVariable @Positive Long userId,
                                                    @PathVariable @Positive Long eventId,
                                                    @RequestBody @Valid EventDtoRequest updateEvent) {
        log.info("обновить события с eventId={} и userId={} на event:{}", eventId, userId, updateEvent);
        return ResponseEntity.status(HttpStatus.OK).body(eventService.updateEventUser(userId, eventId, updateEvent));
    }

    @JsonView({Private.class})
    @GetMapping("{userId}/events/{eventId}/requests")
    public ResponseEntity<RequestListDto> getUserEventRequests(@PathVariable @Positive Long userId,
                                                                         @PathVariable @Positive Long eventId) {
        log.info("получить события для userId={} и eventId={}", userId, eventId);
        return ResponseEntity.status(HttpStatus.OK).body(eventService.getUserEventRequests(userId, eventId));
    }

    @JsonView({Private.class})
    @PatchMapping("{userId}/events/{eventId}/requests")
    public ResponseEntity<EventRequestStatusUpdateResult> approveRequests(@PathVariable @Positive Long userId,
                                                                          @PathVariable @Positive Long eventId,
                                                                          @RequestBody EventRequestStatusUpdate requests) {
        log.info("обработка запросов для eventId={} и userId={}", eventId, userId);
        return ResponseEntity.status(HttpStatus.OK).body(eventService.approveRequests(userId, eventId, requests));
    }
}
