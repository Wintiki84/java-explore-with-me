package ru.practicum.server.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.event.dto.EventDto;
import ru.practicum.server.event.dto.ListEventDto;
import ru.practicum.server.event.dto.UpdateEventAdminRequest;
import ru.practicum.server.event.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/events")
@Validated
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdminEventController {
    private final EventService eventService;
    private static final String FORMATTER = "yyyy-MM-dd HH:mm:ss";

    @GetMapping
    public ResponseEntity<ListEventDto> getEventsByFiltersForAdmin(
            @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<String> states,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) @DateTimeFormat(pattern = FORMATTER) LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = FORMATTER) LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "0") @Min(0) Integer from,
            @RequestParam(defaultValue = "10") @Min(1) Integer size) {
        log.info("получать события по фильтру");
        return ResponseEntity.status(HttpStatus.OK)
                .body(eventService.getEventsByFiltersForAdmin(users, states, categories, rangeStart, rangeEnd,
                        PageRequest.of(from / size, size)));
    }

    @PatchMapping("{eventId}")
    public ResponseEntity<EventDto> updateEventAdmin(@PathVariable @Min(1) Long eventId,
                                                         @RequestBody @Valid UpdateEventAdminRequest updateEvent) {
        log.info("обновление события с eventId={}: {}", eventId, updateEvent);
        return ResponseEntity.status(HttpStatus.OK).body(eventService.updateEventAdmin(eventId, updateEvent));
    }
}
