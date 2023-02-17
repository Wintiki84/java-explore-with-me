package ru.practicum.server.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.server.model.EndpointHit;


@Mapper
public interface StatsMapper {

    @Mapping(
            source = "timestamp",
            target = "timestamp",
            dateFormat = "yyyy-MM-dd HH:mm:ss"
    )
    EndpointHit mapToEndpointHit(EndpointHitDto EndpointHitDto);

    @Mapping(source = "timestamp",
            target = "timestamp",
            dateFormat = "yyyy-MM-dd HH:mm:ss"
    )
    EndpointHitDto mapToEndpointHitDto(EndpointHit endpointHit);
}
