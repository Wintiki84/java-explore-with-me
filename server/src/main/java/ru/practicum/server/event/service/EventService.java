package ru.practicum.server.event.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.server.event.dto.*;
import ru.practicum.server.request.dto.EventRequestStatusUpdate;
import ru.practicum.server.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.server.request.dto.RequestListDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    EventDto addNewEvent(Long userId, NewEventDto eventDto);

    ListEventShortDto getPrivateUserEvents(Long userId, Pageable pageable);

    EventDto getPrivateUserEvent(Long userId, Long eventId);

    EventDto updateEventUser(Long userId, Long eventId, UpdateEventUserRequest updateEvent);

    ListEventDto getEventsByFiltersForAdmin(List<Long> ids, List<String> states, List<Long> categories,
                                                LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    EventDto updateEventAdmin(Long eventId, UpdateEventAdminRequest updateEvent);

    RequestListDto getUserEventRequests(Long userId, Long eventId);

    EventRequestStatusUpdateResult approveRequests(Long userId, Long eventId, EventRequestStatusUpdate requests);

    EventDto getEventByIdPublic(Long eventId, HttpServletRequest servlet);

    ListEventShortDto getEventsByFiltersPublic(String text, List<Long> categories, Boolean paid,
                                               LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable,
                                               Pageable pageable, HttpServletRequest servlet);
}
