package ru.practicum.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ListViewStats;
import ru.practicum.dto.ViewStats;
import ru.practicum.server.mapper.StatsMapper;
import ru.practicum.server.repository.StatsRepository;


import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;
    private final StatsMapper statsMapper;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @NotNull
    @Override
    @Transactional
    public EndpointHitDto addEndpointHit(@NotNull EndpointHitDto EndpointHitDto) {
        return statsMapper.mapToEndpointHitDto
                (statsRepository.save(statsMapper.mapToEndpointHit(EndpointHitDto)));
    }

    @NotNull
    @Override
    @Transactional(readOnly = true)
    public ListViewStats getStats(@NotNull String start, @NotNull String end,
                                  List<String> uris, @NotNull  Boolean unique) {
        LocalDateTime parseStart = LocalDateTime.parse(start, FORMATTER);
        LocalDateTime parseEnd = LocalDateTime.parse(end, FORMATTER);
        List<ViewStats> viewStats;
        if (unique)
                viewStats = (uris == null) ? statsRepository.getUniqueViewStatsByStartAndEndTime(parseStart, parseEnd)
                        : statsRepository.getUniqueUrisViewStatsByStartAndEndTime(parseStart, parseEnd, uris);
        else
                viewStats = (uris == null) ? statsRepository.getViewStatsByStartAndEndTime(parseStart, parseEnd)
                        : statsRepository.getUrisViewStatsByStartAndEndTime(parseStart, parseEnd, uris);

        return ListViewStats.builder().viewStats(viewStats).build();
    }
}
