package ru.practicum.server.compilation.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.server.compilation.dto.CompilationDtoList;
import ru.practicum.server.compilation.dto.CompilationDtoRequest;
import ru.practicum.server.compilation.dto.CompilationDtoResponse;

import javax.validation.constraints.NotNull;

public interface CompilationService {
    @NotNull
    CompilationDtoResponse addCompilation(@NotNull CompilationDtoRequest compilationDto);

    void deleteCompilation(@NotNull Long compId);

    @NotNull
    CompilationDtoResponse updateCompilation(@NotNull Long compId, @NotNull CompilationDtoRequest updateCompilation);

    @NotNull
    CompilationDtoResponse getCompilation(@NotNull Long compId);

    @NotNull
    CompilationDtoList getCompilations(Boolean pinned, Pageable pageable);
}
