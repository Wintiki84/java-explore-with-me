package ru.practicum.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.dto.ViewStats;
import ru.practicum.server.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<EndpointHit, Long> {
    @Query("SELECT new ru.practicum.dto.ViewStats(vs.app, vs.uri, COUNT(vs.ip)) " +
            "FROM EndpointHit AS vs " +
            "WHERE vs.timestamp BETWEEN :start AND :end " +
            "GROUP BY vs.app, vs.uri " +
            "ORDER BY COUNT(vs.ip) DESC")
    List<ViewStats> getViewStats(@Param("start") LocalDateTime start,
                                 @Param("end") LocalDateTime end);

    @Query("SELECT new ru.practicum.dto.ViewStats(vs.app, vs.uri, COUNT(vs.ip)) " +
            "FROM EndpointHit AS vs " +
            "WHERE vs.timestamp BETWEEN :start AND :end " +
            "AND vs.uri IN :uris " +
            "GROUP BY vs.app, vs.uri " +
            "ORDER BY COUNT(vs.ip) DESC")
    List<ViewStats> getUrisViewStats(@Param("start") LocalDateTime start,
                                     @Param("end") LocalDateTime end,
                                     @Param("uris") String[] uris);

    @Query("SELECT new ru.practicum.dto.ViewStats(vs.app, vs.uri, COUNT(DISTINCT vs.ip)) " +
            "FROM EndpointHit AS vs " +
            "WHERE vs.timestamp BETWEEN :start AND :end " +
            "GROUP BY vs.app, vs.uri " +
            "ORDER BY COUNT(DISTINCT vs.ip) DESC")
    List<ViewStats> getUniqueViewStats(@Param("start") LocalDateTime start,
                                       @Param("end") LocalDateTime end);

    @Query("SELECT new ru.practicum.dto.ViewStats(vs.app, vs.uri, COUNT(DISTINCT vs.ip)) " +
            "FROM EndpointHit AS vs " +
            "WHERE vs.timestamp BETWEEN :start AND :end " +
            "AND vs.uri IN :uris " +
            "GROUP BY vs.app, vs.uri " +
            "ORDER BY COUNT(DISTINCT vs.ip) DESC")
    List<ViewStats> getUniqueUrisViewStats(@Param("start") LocalDateTime start,
                                           @Param("end") LocalDateTime end,
                                           @Param("uris") String[] uris);
}
