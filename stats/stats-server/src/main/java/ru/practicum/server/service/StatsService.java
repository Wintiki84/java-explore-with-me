package ru.practicum.server.service;


import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ListViewStats;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface StatsService {
    @NotNull
    EndpointHitDto addEndpointHit(@NotNull EndpointHitDto EndpointHitDto);

    @NotNull
    ListViewStats getStats(@NotNull String start, @NotNull String end,
                           List<String> uris, @NotNull Boolean unique);
}
