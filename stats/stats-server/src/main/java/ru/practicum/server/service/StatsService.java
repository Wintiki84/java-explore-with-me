package ru.practicum.server.service;

import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStats;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface StatsService {
    @NotNull
    EndpointHitDto addEndpointHit(@NotNull EndpointHitDto endpointHitDto);

    @NotNull
    List<ViewStats> getStats(@NotNull String start, @NotNull String end,
                             String[] uris, @NotNull Boolean unique);
}
