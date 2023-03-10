package ru.practicum.server.request.service;

import ru.practicum.server.request.dto.RequestDto;
import ru.practicum.server.request.dto.RequestListDto;

import javax.validation.constraints.NotNull;

public interface RequestService {
    @NotNull
    RequestDto createRequest(@NotNull Long userId, @NotNull Long eventId);

    @NotNull
    RequestListDto getUserRequests(@NotNull Long userId);

    @NotNull
    RequestDto canceledRequest(@NotNull Long userId, @NotNull Long eventId);
}
