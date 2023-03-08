package ru.practicum.server.compilation.service;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.server.compilation.dto.CompilationDtoList;
import ru.practicum.server.compilation.dto.CompilationDtoRequest;
import ru.practicum.server.compilation.dto.CompilationDtoResponse;
import ru.practicum.server.compilation.mapper.CompilationMapper;
import ru.practicum.server.compilation.model.Compilation;
import ru.practicum.server.compilation.model.QCompilation;
import ru.practicum.server.compilation.repository.CompilationRepository;
import ru.practicum.server.event.model.Event;
import ru.practicum.server.event.repository.EventRepository;
import ru.practicum.server.handler.exception.NotFoundException;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CompilationServiceImp implements CompilationService {
    private final CompilationRepository compilations;
    private final CompilationMapper mapper;
    private final EventRepository events;

    @NotNull
    @Override
    @Transactional
    public CompilationDtoResponse addCompilation(@NotNull CompilationDtoRequest compilationDto) {
        Set<Event> findEvents = events.findAllByIdIn(compilationDto.getEvents());
        Compilation compilation = mapper.mapToCompilation(compilationDto);
        compilation.setEvents(findEvents);
        return mapper.mapToCompilationDtoResponse(compilations.save(compilation));
    }

    @NotNull
    @Override
    @Transactional
    public void deleteCompilation(@NotNull Long compId) {
        findByCompilationId(compId);
        compilations.deleteById(compId);
    }

    @NotNull
    @Override
    @Transactional
    public CompilationDtoResponse updateCompilation(@NotNull Long compId, @NotNull CompilationDtoRequest updateCompilation) {
        Compilation compilation = findByCompilationId(compId);
        Set<Event> findEvents = events.findAllByIdIn(updateCompilation.getEvents());
        compilation = mapper.mapToCompilation(updateCompilation, compilation);
        compilation.setEvents(findEvents);
        return mapper.mapToCompilationDtoResponse(compilations.save(compilation));
    }

    @NotNull
    @Override
    @Transactional(readOnly = true)
    public CompilationDtoResponse getCompilation(@NotNull Long compId) {
        return mapper.mapToCompilationDtoResponse(findByCompilationId(compId));
    }

    @NotNull
    @Override
    @Transactional(readOnly = true)
    public CompilationDtoList getCompilations(Boolean pinned, Pageable pageable) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        Page<Compilation> page;
        if (pinned != null) {
            booleanBuilder.and(QCompilation.compilation.pinned.eq(pinned));
        }
        if (booleanBuilder.getValue() != null) {
            page = compilations.findAll(booleanBuilder.getValue(), pageable);
        } else {
            page = compilations.findAll(pageable);
        }
        return CompilationDtoList
                .builder()
                .compilations(mapper.mapToCompilationDtoResponseList(page.getContent()))
                .build();
    }

    private Compilation findByCompilationId(Long compId) {
        return compilations.findById(compId)
                .orElseThrow(() -> new NotFoundException("Компиляция с id=" + compId + " не найдена"));
    }
}
