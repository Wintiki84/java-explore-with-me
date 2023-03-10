package ru.practicum.server.event.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.server.event.dto.*;
import ru.practicum.server.request.dto.EventRequestStatusUpdate;
import ru.practicum.server.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.server.request.dto.RequestListDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    @NotNull
    EventDtoResponse addEvent(@NotNull Long userId, @NotNull EventDtoRequest eventDto);

    @NotNull
    ListEventShortDto getPrivateUserEvents(@NotNull Long userId, @NotNull Pageable pageable);

    @NotNull
    EventDtoResponse getPrivateUserEvent(@NotNull Long userId, @NotNull Long eventId);

    @NotNull
    EventDtoResponse updateEventUser(@NotNull Long userId, @NotNull Long eventId, @NotNull EventDtoRequest updateEvent);

    @NotNull
    ListEventDtoResponse getEventsByFiltersForAdmin(List<Long> ids, List<String> states, List<Long> categories,
                                                LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                            @NotNull Pageable pageable);

    @NotNull
    EventDtoResponse updateEventAdmin(@NotNull Long eventId, @NotNull EventDtoRequest updateEvent);

    @NotNull
    RequestListDto getUserEventRequests(@NotNull Long userId, @NotNull Long eventId);

    @NotNull
    EventRequestStatusUpdateResult approveRequests(@NotNull Long userId, @NotNull Long eventId,
                                                   @NotNull EventRequestStatusUpdate requests);

    @NotNull
    EventDtoResponse getEventByIdPublic(@NotNull Long eventId, @NotNull HttpServletRequest servlet);

    @NotNull
    ListEventShortDto getEventsByFiltersPublic(String text, List<Long> categories, Boolean paid,
                                               LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable,
                                               @NotNull Pageable pageable, @NotNull HttpServletRequest servlet);
}
