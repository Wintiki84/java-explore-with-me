package ru.practicum.server.controller;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.validator.*;
import ru.practicum.dto.ListViewStats;
import ru.practicum.server.service.StatsService;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class StatsController {
    private final StatsService statsService;

    @JsonView({Details.class})
    @PostMapping("/hit")
    public ResponseEntity<EndpointHitDto> addEndpointHit(
            @NotNull @RequestBody EndpointHitDto endpointHitDto) {
        log.info("Запрос на добавление EndpointHit: {}", endpointHitDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(statsService.addEndpointHit(endpointHitDto));
    }

    @JsonView({AdminDetails.class})
    @GetMapping("/stats")
    public ResponseEntity<ListViewStats> getStats(@NotEmpty @DateValidator
                                                  @RequestParam String start,
                                                  @NotEmpty @DateValidator
                                                  @RequestParam String end,
                                                  @RequestParam(required = false) List<String> uris,
                                                  @RequestParam(defaultValue = "false") Boolean unique) {
        log.info("Запрос на чтение статистики:start {}, end {}", start, end);
        return ResponseEntity.status(HttpStatus.OK).body(statsService.getStats(start, end, uris, unique));
    }
}
