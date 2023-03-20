package ru.practicum.server.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.server.model.EndpointHit;

import static ru.practicum.constants.Constants.DATE_FORMAT;


@Mapper
public interface StatsMapper {

    @Mapping(
            source = "timestamp",
            target = "timestamp",
            dateFormat = DATE_FORMAT
    )
    EndpointHit mapToEndpointHit(EndpointHitDto endpointHitDto);

    @Mapping(source = "timestamp",
            target = "timestamp",
            dateFormat = DATE_FORMAT
    )
    EndpointHitDto mapToEndpointHitDto(EndpointHit endpointHit);
}
