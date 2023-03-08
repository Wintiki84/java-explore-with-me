package ru.practicum.server.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.server.event.enums.State;
import ru.practicum.server.event.model.Event;
import ru.practicum.server.event.repository.EventRepository;
import ru.practicum.server.handler.exception.AccessException;
import ru.practicum.server.handler.exception.NotFoundException;
import ru.practicum.server.request.dto.RequestDto;
import ru.practicum.server.request.dto.RequestListDto;
import ru.practicum.server.request.enums.RequestStatus;
import ru.practicum.server.request.maper.RequestMapper;
import ru.practicum.server.request.model.Request;
import ru.practicum.server.request.repository.RequestRepository;
import ru.practicum.server.user.model.User;
import ru.practicum.server.user.repository.UserRepository;

import javax.validation.constraints.NotNull;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RequestServiceImp implements RequestService {
    private final RequestRepository requests;
    private final UserRepository users;
    private final EventRepository events;
    private final RequestMapper mapper;

    @Override
    @Transactional
    @NotNull
    public RequestDto createRequest(@NotNull Long userId, @NotNull Long eventId) {
        Event event = findByEventId(eventId);
        User requester = findByUserId(userId);
        validateCreatingRequest(event, userId);
        if (event.getParticipantLimit() == 0 || event.getParticipantLimit() > event.getConfirmedRequests()) {
            var newRequest = preparationRequest(event, requester);
            if (!event.getRequestModeration()) {
                newRequest.setStatus(RequestStatus.CONFIRMED);
                event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                events.save(event);
            } else {
                newRequest.setStatus(RequestStatus.PENDING);
            }
            return mapper.mapToRequestDto(requests.save(newRequest));
        } else {
            throw new AccessException("Достигнут лимит запросов");
        }
    }

    @Override
    @Transactional(readOnly = true)
    @NotNull
    public RequestListDto getUserRequests(@NotNull Long userId) {
        findByUserId(userId);
            return RequestListDto
                    .builder()
                    .requests(mapper.mapToRequestDtoList(requests.findAllByRequesterId(userId)))
                    .build();
    }

    @Override
    @Transactional
    @NotNull
    public RequestDto canceledRequest(@NotNull Long userId, @NotNull Long requestId) {
        findByUserId(userId);
        Request request = findByRequestId(requestId);
        Event event = request.getEvent();
        if (request.getStatus().equals(RequestStatus.CONFIRMED)) {
            event.setConfirmedRequests(event.getConfirmedRequests() - 1);
            request.setStatus(RequestStatus.CANCELED);
            events.save(event);
        } else {
            request.setStatus(RequestStatus.CANCELED);
        }
        return mapper.mapToRequestDto(requests.save(request));
    }

    private Request preparationRequest(Event event, User requester) {
        Request newRequest = new Request();
        newRequest.setEvent(event);
        newRequest.setRequester(requester);
        return newRequest;
    }

    private void validateCreatingRequest(Event event, Long userId) {
        if (event.getInitiator().getId().equals(userId)) {
            throw new AccessException("Вы не можете создать запрос на участие в вашем собственном мероприятии");
        }
        if (event.getState() != State.PUBLISHED) {
            throw new AccessException("Вы не можете участвовать в неопубликованном мероприятии");
        }
    }

    private User findByUserId(Long userId) {
        return users.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=" + userId + " не найден"));
    }

    private Event findByEventId(Long eventId) {
        return events.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие с id=" + eventId + " не найдено"));
    }

    private Request findByRequestId(Long requestId) {
        return requests.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос с id=" + requestId + " не найден"));
    }
}
