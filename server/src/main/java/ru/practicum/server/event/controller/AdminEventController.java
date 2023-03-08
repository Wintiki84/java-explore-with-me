package ru.practicum.server.event.controller;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.event.dto.EventDtoRequest;
import ru.practicum.server.event.dto.EventDtoResponse;
import ru.practicum.server.event.dto.ListEventDtoResponse;
import ru.practicum.server.event.service.EventService;
import ru.practicum.validator.AdminDetails;
import ru.practicum.validator.Update;

import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.constants.constants.DATE_FORMAT;

@RestController
@RequestMapping("/admin/events")
@Validated
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdminEventController {
    private final EventService eventService;

    @JsonView(AdminDetails.class)
    @GetMapping
    public ResponseEntity<ListEventDtoResponse> getEventsByFiltersForAdmin(
            @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<String> states,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_FORMAT) LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_FORMAT) LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "0") @Min(0) Integer from,
            @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("получить события по фильтру");
        return ResponseEntity.status(HttpStatus.OK)
                .body(eventService.getEventsByFiltersForAdmin(users, states, categories, rangeStart, rangeEnd,
                        PageRequest.of(from / size, size)));
    }

    @JsonView(AdminDetails.class)
    @PatchMapping("{eventId}")
    public ResponseEntity<EventDtoResponse> updateEventAdmin(@PathVariable @Positive Long eventId,
                                                             @RequestBody @Validated(Update.class) EventDtoRequest updateEvent) {
        log.info("обновление события с eventId={}: {}", eventId, updateEvent);
        return ResponseEntity.status(HttpStatus.OK).body(eventService.updateEventAdmin(eventId, updateEvent));
    }
}
