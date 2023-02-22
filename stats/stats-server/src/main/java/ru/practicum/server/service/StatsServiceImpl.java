package ru.practicum.server.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStats;
import ru.practicum.server.mapper.StatsMapper;
import ru.practicum.server.repository.StatsRepository;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;
    private final StatsMapper statsMapper;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @NotNull
    @Override
    @Transactional
    public EndpointHitDto addEndpointHit(@NotNull EndpointHitDto endpointHitDto) {
        return statsMapper.mapToEndpointHitDto
                (statsRepository.save(statsMapper.mapToEndpointHit(endpointHitDto)));
    }

    @NotNull
    @Override
    @Transactional(readOnly = true)
    public List<ViewStats> getStats(@NotNull String start, @NotNull String end,
                                    String[] uris, @NotNull  Boolean unique) {
        LocalDateTime parseStart = LocalDateTime.parse(start, FORMATTER);
        LocalDateTime parseEnd = LocalDateTime.parse(end, FORMATTER);
        if (unique) {
            return uris == null
                    ? statsRepository.getUniqueViewStats(parseStart, parseEnd)
                    : statsRepository.getUniqueUrisViewStats(parseStart, parseEnd, uris);
        } else {
            return uris == null
                    ? statsRepository.getViewStats(parseStart, parseEnd)
                    : statsRepository.getUrisViewStats(parseStart, parseEnd, uris);
        }
    }
}
