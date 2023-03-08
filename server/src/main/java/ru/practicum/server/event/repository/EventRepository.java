package ru.practicum.server.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.server.event.enums.State;
import ru.practicum.server.event.model.Event;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface EventRepository extends PagingAndSortingRepository<Event, Long>, QuerydslPredicateExecutor<Event> {
    List<Event> findAllByInitiatorId(Long userId, Pageable pageable);

    Optional<Event> findByIdAndInitiatorId(Long eventId, Long userId);

    Set<Event> findAllByIdIn(List<Long> eventIds);

    Optional<Event> findByIdAndState(Long eventId, State state);
}
