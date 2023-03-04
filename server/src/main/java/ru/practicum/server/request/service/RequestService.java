package ru.practicum.server.request.service;

import ru.practicum.server.request.dto.RequestDto;
import ru.practicum.server.request.dto.RequestListDto;

public interface RequestService {
    RequestDto createRequest(Long userId, Long eventId);

    RequestListDto getUserRequests(Long userId);

    RequestDto canceledRequest(Long userId, Long eventId);
}
