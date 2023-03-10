package ru.practicum.server.compilation.mapper;

import org.mapstruct.*;
import ru.practicum.server.compilation.dto.CompilationDtoRequest;
import ru.practicum.server.compilation.dto.CompilationDtoResponse;
import ru.practicum.server.compilation.model.Compilation;
import ru.practicum.server.event.mapper.EventMapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = EventMapper.class)
public interface CompilationMapper {
    @Mapping(target = "events", ignore = true)
    Compilation mapToCompilation(CompilationDtoRequest compilationDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "events", ignore = true)
    Compilation mapToCompilation(CompilationDtoRequest updateCompilation, @MappingTarget Compilation compilation);

    CompilationDtoResponse mapToCompilationDtoResponse(Compilation compilation);

    List<CompilationDtoResponse> mapToCompilationDtoResponseList(List<Compilation> compilations);
}
