package ru.practicum.server.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.event.dto.EventDto;
import ru.practicum.server.event.dto.ListEventShortDto;
import ru.practicum.server.event.dto.NewEventDto;
import ru.practicum.server.event.dto.UpdateEventUserRequest;
import ru.practicum.server.event.service.EventService;
import ru.practicum.server.request.dto.EventRequestStatusUpdate;
import ru.practicum.server.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.server.request.dto.RequestListDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequestMapping("/users")
@Validated
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PrivateEventController {
    private final EventService eventService;

    @PostMapping("{userId}/events")
    public ResponseEntity<EventDto> addEvent(@PathVariable @Min(1) Long userId,
                                                 @RequestBody @Valid NewEventDto eventDto) {
        log.info("добваитьс новое событие ползователя с id={}: {}", userId, eventDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.addNewEvent(userId, eventDto));
    }

    @GetMapping("{userId}/events")
    public ResponseEntity<ListEventShortDto> getUserEvents(@RequestParam(defaultValue = "0") @Min(0) Integer from,
                                                           @RequestParam(defaultValue = "10") @Min(1) Integer size,
                                                           @PathVariable @Min(1) Long userId) {
        log.info("получить события userId={}, from: {},size: {}", userId, from, size);
        return ResponseEntity.status(HttpStatus.OK)
                .body(eventService.getPrivateUserEvents(userId, PageRequest.of(from / size, size)));
    }

    @GetMapping("{userId}/events/{eventId}")
    public ResponseEntity<EventDto> getUserEvent(@PathVariable @Min(1) Long userId,
                                                     @PathVariable @Min(1) Long eventId) {
        log.info("получить события с eventId={} и userId={}", eventId, userId);
        return ResponseEntity.status(HttpStatus.OK).body(eventService.getPrivateUserEvent(userId, eventId));
    }

    @PatchMapping("{userId}/events/{eventId}")
    public ResponseEntity<EventDto> updateEvent(@PathVariable @Min(1) Long userId,
                                                    @PathVariable @Min(1) Long eventId,
                                                    @RequestBody @Valid UpdateEventUserRequest updateEvent) {
        log.info("обновить события с eventId={} и userId={} на event:{}", eventId, userId, updateEvent);
        return ResponseEntity.status(HttpStatus.OK).body(eventService.updateEventUser(userId, eventId, updateEvent));
    }

    @GetMapping("{userId}/events/{eventId}/requests")
    public ResponseEntity<RequestListDto> getUserEventRequests(@PathVariable @Min(1) Long userId,
                                                                         @PathVariable @Min(1) Long eventId) {
        log.info("получиьть события для userId={} и eventId={}", userId, eventId);
        return ResponseEntity.status(HttpStatus.OK).body(eventService.getUserEventRequests(userId, eventId));
    }

    @PatchMapping("{userId}/events/{eventId}/requests")
    public ResponseEntity<EventRequestStatusUpdateResult> approveRequests(@PathVariable @Min(1) Long userId,
                                                                          @PathVariable @Min(1) Long eventId,
                                                                          @RequestBody EventRequestStatusUpdate requests) {
        log.info("обработка запросов для eventId={} и userId={}", eventId, userId);
        return ResponseEntity.status(HttpStatus.OK).body(eventService.approveRequests(userId, eventId, requests));
    }
}
