package ru.practicum.server.event.controller;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.event.dto.EventDtoResponse;
import ru.practicum.server.event.dto.ListEventShortDto;
import ru.practicum.server.event.enums.EventSort;
import ru.practicum.server.event.service.EventService;
import ru.practicum.validator.Details;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.constants.Constants.DATE_FORMAT;

@RestController
@RequestMapping("/events")
@Validated
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PublicEventController {
    private final EventService eventService;

    @JsonView({Details.class})
    @GetMapping("{id}")
    public ResponseEntity<EventDtoResponse> getEventById(@PathVariable("id") @Min(1) Long eventId,
                                                         HttpServletRequest servlet) {
        log.info("получить события с id={}", eventId);
        return ResponseEntity.status(HttpStatus.OK).body(eventService.getEventByIdPublic(eventId, servlet));
    }

    @JsonView({Details.class})
    @GetMapping
    public ResponseEntity<ListEventShortDto> getEventsPublic(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_FORMAT) LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_FORMAT) LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = "0") @Min(0) Integer from,
            @RequestParam(defaultValue = "10") @Min(1) Integer size,
            HttpServletRequest servlet) {
        log.info("сделать события общедоступными");
        return ResponseEntity.status(HttpStatus.OK)
                .body(eventService.getEventsByFiltersPublic(
                        text, categories, paid, rangeStart, rangeEnd, onlyAvailable,
                        PageRequest.of(from / size, size, Sort.by(EventSort.getSortField(sort)).ascending()), servlet));
    }
}
