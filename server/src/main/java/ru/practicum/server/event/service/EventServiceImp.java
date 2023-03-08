package ru.practicum.server.event.service;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.server.category.model.Category;
import ru.practicum.server.category.repository.CategoryRepository;
import ru.practicum.server.event.dto.*;
import ru.practicum.server.event.enums.State;
import ru.practicum.server.event.enums.StateAction;
import ru.practicum.server.event.mapper.EventMapper;
import ru.practicum.server.event.model.Event;
import ru.practicum.server.event.model.QEvent;
import ru.practicum.server.event.repository.EventRepository;
import ru.practicum.server.event.statclient.StatisticClient;
import ru.practicum.server.handler.exception.AccessException;
import ru.practicum.server.handler.exception.EventStateException;
import ru.practicum.server.handler.exception.NotFoundException;
import ru.practicum.server.request.dto.EventRequestStatusUpdate;
import ru.practicum.server.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.server.request.dto.RequestDto;
import ru.practicum.server.request.dto.RequestListDto;
import ru.practicum.server.request.enums.RequestStatus;
import ru.practicum.server.request.maper.RequestMapper;
import ru.practicum.server.request.repository.RequestRepository;
import ru.practicum.server.user.model.User;
import ru.practicum.server.user.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.constants.constants.DATE_FORMAT;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EventServiceImp implements EventService {
    private final EventRepository events;
    private final UserRepository users;
    private final CategoryRepository categories;
    private final EventMapper mapper;
    private final RequestMapper requestMapper;
    private final StatisticClient statisticClient;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
    private final RequestRepository requestRepository;

    @Override
    @Transactional
    @NotNull
    public EventDtoResponse addEvent(@NotNull Long userId, @NotNull EventDtoRequest eventDto) {
        User user = findByUserId(userId);
        Category category = findByCategoryId(eventDto.getCategory());
        Event newEvent = mapper.mapToEvent(eventDto);
        validationDate(newEvent.getEventDate());
            newEvent.setInitiator(user);
            newEvent.setCategory(category);
            newEvent.setCreatedOn(LocalDateTime.now());
            return mapper.mapToEventDtoResponse(events.save(newEvent));
    }

    @Override
    @Transactional(readOnly = true)
    @NotNull
    public ListEventShortDto getPrivateUserEvents(@NotNull Long userId, @NotNull Pageable pageable) {
        findByUserId(userId);
        return ListEventShortDto
                    .builder()
                    .events(mapper.mapToListEventShortDto(events.findAllByInitiatorId(userId, pageable)))
                    .build();
    }

    @Override
    @Transactional(readOnly = true)
    @NotNull
    public EventDtoResponse getPrivateUserEvent(@NotNull Long userId, @NotNull Long eventId) {
        findByUserId(userId);
        return mapper.mapToEventDtoResponse(findByEventId(eventId, userId));
    }

    @Override
    @Transactional
    @NotNull
    public EventDtoResponse updateEventUser(@NotNull Long userId, @NotNull Long eventId,
                                    @NotNull EventDtoRequest updateEvent) {
        findByUserId(userId);
            LocalDateTime eventTime;
            if (updateEvent.getEventDate() != null) {
                eventTime = LocalDateTime.parse(updateEvent.getEventDate(), formatter);
                validationDate(eventTime);
            }
            Event event = findByEventId(eventId, userId);
            if (event.getState().equals(State.PUBLISHED)) {
                throw new AccessException("Могут быть изменены только ожидающие или отмененные события");
            }
            if (updateEvent.getCategory() != null) {
                event.setCategory(findByCategoryId(updateEvent.getCategory()));
            }
            event.setState(StateAction.getState(updateEvent.getStateAction()));
            return mapper.mapToEventDtoResponse(events.save(mapper.mapToEvent(updateEvent, event)));
    }

    @Override
    @Transactional(readOnly = true)
    @NotNull
    public ListEventDtoResponse getEventsByFiltersForAdmin(List<Long> ids, List<String> states,
                                                   List<Long> categories, LocalDateTime rangeStart,
                                                   LocalDateTime rangeEnd, @NotNull Pageable pageable) {
        BooleanBuilder booleanBuilder = createQuery(ids, states, categories, rangeStart, rangeEnd);
        Page<Event> page;
        if (booleanBuilder.getValue() != null) {
            page = events.findAll(booleanBuilder, pageable);
        } else {
            page = events.findAll(pageable);
        }
        return ListEventDtoResponse
                .builder()
                .events(mapper.mapToListEventDtoResponse(page.getContent()))
                .build();
    }

    @Override
    @Transactional
    @NotNull
    public EventDtoResponse updateEventAdmin(@NotNull Long eventId, @NotNull EventDtoRequest updateEvent) {
        LocalDateTime eventTime;
        if (updateEvent.getEventDate() != null) {
            eventTime = LocalDateTime.parse(updateEvent.getEventDate(), formatter);
            validationDate(eventTime);
        }
        Event event = events.findById(eventId).orElseThrow(
                () -> new NotFoundException("Событие с id=" + eventId + " не найдено"));
        changeEventState(event, updateEvent.getStateAction());
        if (updateEvent.getCategory() != null) {
            event.setCategory(findByCategoryId(updateEvent.getCategory()));
        }
        return mapper.mapToEventDtoResponse(events.save(mapper.mapToEvent(updateEvent, event)));
    }

    @Override
    @Transactional(readOnly = true)
    @NotNull
    public RequestListDto getUserEventRequests(@NotNull Long userId, @NotNull Long eventId) {
        findByUserId(userId);
            Event event = findByEventId(eventId, userId);
            return RequestListDto
                    .builder()
                    .requests(event.getRequests().stream().map(requestMapper::mapToRequestDto)
                            .collect(Collectors.toList()))
                    .build();
    }

    @Override
    @Transactional
    @NotNull
    public EventRequestStatusUpdateResult approveRequests(@NotNull Long userId, @NotNull Long eventId,
                                                          @NotNull EventRequestStatusUpdate requests) {
        findByUserId(userId);
            Event event = findByEventId(eventId, userId);
            if (event.getParticipantLimit() <= event.getConfirmedRequests()) {
                throw new AccessException("Лимит участников достигнут");
            }
            List<RequestDto> confirmedRequests = new ArrayList<>();
            List<RequestDto> rejectedRequests = new ArrayList<>();
            moderationRequests(confirmedRequests, rejectedRequests, event, requests);
            return EventRequestStatusUpdateResult
                    .builder()
                    .confirmedRequests(confirmedRequests)
                    .rejectedRequests(rejectedRequests)
                    .build();
    }

    @Override
    @Transactional
    @NotNull
    public EventDtoResponse getEventByIdPublic(@NotNull Long eventId, @NotNull HttpServletRequest servlet) {
        statisticClient.postStats(servlet, "ewm-server");
        Event event = findByEventId(eventId, null);
        event.setViews(statisticClient.getViews(eventId));
        return mapper.mapToEventDtoResponse(events.save(event));
    }

    @Override
    @Transactional(readOnly = true)
    @NotNull
    public ListEventShortDto getEventsByFiltersPublic(String text, List<Long> categories,
                                                      Boolean paid,
                                                      LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                      Boolean onlyAvailable,
                                                      @NotNull Pageable pageable, @NotNull HttpServletRequest servlet) {
        statisticClient.postStats(servlet, "ewm-server");
        BooleanBuilder booleanBuilder = createQuery(null, null, categories, rangeStart, rangeEnd);
        Page<Event> page;
        if (text != null) {
            booleanBuilder.and(QEvent.event.annotation.likeIgnoreCase(text))
                    .or(QEvent.event.description.likeIgnoreCase(text));
        }
        if (paid != null) {
            booleanBuilder.and(QEvent.event.paid.eq(paid));
        }
        if (rangeStart == null && rangeEnd == null) {
            booleanBuilder.and(QEvent.event.eventDate.after(LocalDateTime.now()));
        }
        if (onlyAvailable) {
            booleanBuilder.and((QEvent.event.participantLimit.eq(0)))
                    .or(QEvent.event.participantLimit.gt(QEvent.event.confirmedRequests));
        }
        if (booleanBuilder.getValue() != null) {
            page = events.findAll(booleanBuilder.getValue(), pageable);
        } else {
            page = events.findAll(pageable);
        }
        return ListEventShortDto
                .builder()
                .events(mapper.mapToListEventShortDto(page.getContent()))
                .build();
    }

    private BooleanBuilder createQuery(List<Long> ids, List<String> states, List<Long> categories,
                                       LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (ids != null && !ids.isEmpty()) {
            booleanBuilder.and(QEvent.event.initiator.id.in(ids));
        }
        if (states != null && !states.isEmpty()) {
            try {
                booleanBuilder.and(QEvent.event.state.in(states.stream().map(State::valueOf).collect(Collectors.toList())));
            } catch (Exception e) {
                log.info(e.getMessage());
            }
        }
        if (categories != null && !categories.isEmpty()) {
            booleanBuilder.and(QEvent.event.category.id.in(categories));
        }
        if (rangeStart != null) {
            booleanBuilder.and(QEvent.event.eventDate.after(rangeStart));
        }
        if (rangeEnd != null) {
            booleanBuilder.and(QEvent.event.eventDate.before(rangeEnd));
        }
        return booleanBuilder;
    }

    private void moderationRequests(List<RequestDto> confirmedRequests,
                                    List<RequestDto> rejectedRequests,
                                    Event event, EventRequestStatusUpdate requests) {
        requestRepository.findAllByIdIn(requests.getRequestIds()).stream().peek(r -> {
            if (r.getStatus().equals(RequestStatus.PENDING)) {
                if (event.getParticipantLimit() == 0) {
                    r.setStatus(RequestStatus.CONFIRMED);
                    event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                } else if (event.getParticipantLimit() > event.getConfirmedRequests()) {
                    if (!event.getRequestModeration()) {
                        r.setStatus(RequestStatus.CONFIRMED);
                        event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                    } else {
                        if (requests.getStatus().equals(RequestStatus.CONFIRMED.toString())) {
                            r.setStatus(RequestStatus.CONFIRMED);
                            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                        } else {
                            r.setStatus(RequestStatus.REJECTED);
                        }
                    }
                } else {
                    r.setStatus(RequestStatus.REJECTED);
                }
            } else {
                throw new AccessException("Может только подтверждать PENDING запросы");
            }
        }).map(requestMapper::mapToRequestDto).forEach(r -> {
            if (r.getStatus().equals(RequestStatus.CONFIRMED)) {
                confirmedRequests.add(r);
            } else {
                rejectedRequests.add(r);
            }
        });
    }

    private void changeEventState(Event event, String actionState) {
        switch (StateAction.getState(actionState)) {
            case PUBLISHED:
                if (event.getState().equals(State.PENDING)) {
                    event.setState(State.PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                    break;
                } else {
                    throw new EventStateException("не правильное состояние события:" +
                            event.getState());
                }
            case CANCELED:
                if (event.getState().equals(State.PENDING)) {
                    event.setState(State.CANCELED);
                    break;
                } else {
                    throw new EventStateException("не правильное состояние события: " +
                            event.getState());
                }
        }
    }

    private User findByUserId(Long userId) {
        return users.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=" + userId + " не найден"));
    }

    private Category findByCategoryId(Long categoryId) {
        return categories.findById(categoryId).orElseThrow(
                () -> new NotFoundException("Категория с id=" + categoryId + " не найдена"));
    }

    private Event findByEventId(Long eventId, Long userId) {
        if (userId != null)
            return events.findByIdAndInitiatorId(eventId, userId)
                    .orElseThrow(() -> new NotFoundException("Событие с id=" + eventId + "для пользователя с id="
                            + userId + " не найдено"));
        else
            return events.findByIdAndState(eventId, State.PUBLISHED)
                .orElseThrow(() -> new NotFoundException("Событие с id=" + eventId + " State.PUBLISHED не найдено"));
    }

    private void validationDate(LocalDateTime eventTime) {
        if (eventTime.isBefore(LocalDateTime.now())) {
            throw new AccessException("Поле: eventDate. Ошибка: должно содержать дату, которая еще не наступила. " +
                    "Value: " + eventTime);
        }
    }
}
