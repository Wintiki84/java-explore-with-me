package ru.practicum.server.request.maper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.server.request.dto.RequestDto;
import ru.practicum.server.request.model.Request;

import java.util.List;

import static ru.practicum.constants.constants.DATE_FORMAT;

@Mapper(componentModel = "spring")
public interface RequestMapper {
    @Mapping(source = "created", target = "created", dateFormat = DATE_FORMAT)
    @Mapping(source = "event.id", target = "event")
    @Mapping(source = "requester.id", target = "requester")
    RequestDto mapToRequestDto(Request request);

    List<RequestDto> mapToRequestDtoList(List<Request> requests);
}
