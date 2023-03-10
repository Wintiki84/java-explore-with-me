package ru.practicum.server.compilation.controller;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.compilation.dto.CompilationDtoList;
import ru.practicum.server.compilation.dto.CompilationDtoResponse;
import ru.practicum.server.compilation.service.CompilationService;
import ru.practicum.validator.Details;

import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/compilations")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Validated
public class PublicCompilationController {
    private final CompilationService compilationService;

    @JsonView(Details.class)
    @GetMapping("{compId}")
    public ResponseEntity<CompilationDtoResponse> getCompilation(@PathVariable @Positive Long compId) {
        log.info("получить компиляцию с id={}", compId);
        return ResponseEntity.status(HttpStatus.OK).body(compilationService.getCompilation(compId));
    }

    @JsonView(Details.class)
    @GetMapping
    public ResponseEntity<CompilationDtoList> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                              @RequestParam(defaultValue = "10") @Positive Integer size,
                                                              @RequestParam(defaultValue = "0") @Min(0) Integer from) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(compilationService.getCompilations(pinned, PageRequest.of(from / size, size)));
    }
}
