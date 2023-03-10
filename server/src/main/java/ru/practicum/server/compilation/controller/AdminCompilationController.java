package ru.practicum.server.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.compilation.dto.CompilationDtoRequest;
import ru.practicum.server.compilation.dto.CompilationDtoResponse;
import ru.practicum.server.compilation.service.CompilationService;
import ru.practicum.validator.Create;
import ru.practicum.validator.Update;

import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/admin/compilations")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Validated
public class AdminCompilationController {
    private final CompilationService compilationService;

    @PostMapping
    public ResponseEntity<CompilationDtoResponse> addCompilation(@RequestBody @Validated(Create.class)
                                                                     CompilationDtoRequest compilation) {
        log.info("добавить компиляцию:{}", compilation);
        return ResponseEntity.status(HttpStatus.CREATED).body(compilationService.addCompilation(compilation));
    }

    @DeleteMapping("{compId}")
    public ResponseEntity<Void> deleteCompilation(@PathVariable @Positive Long compId) {
        log.info("удалить компиляцию с id={}", compId);
        compilationService.deleteCompilation(compId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("{compId}")
    public ResponseEntity<CompilationDtoResponse> updateCompilation(@PathVariable @Positive Long compId,
                                                                @RequestBody @Validated(Update.class)
                                                                CompilationDtoRequest updateCompilation) {
        log.info("обнавить компиляцию с id={} на компиляцию:{}", compId, updateCompilation);
        return ResponseEntity.status(HttpStatus.OK).body(compilationService.updateCompilation(compId, updateCompilation));
    }
}
