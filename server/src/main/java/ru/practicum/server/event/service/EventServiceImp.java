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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final RequestRepository requestRepository;

    @Override
    @Transactional
    public EventDto addNewEvent(Long userId, NewEventDto eventDto) {
        User user = users.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=" + userId + " не найден"));
        Category category = categories.findById(eventDto.getCategory()).orElseThrow(
                () -> new NotFoundException("Категория с id=" + eventDto.getCategory() + " не найдена"));
        Event newEvent = mapper.mapToEvent(eventDto);
        if (newEvent.getEventDate().isBefore(LocalDateTime.now().minusHours(2))) {
            throw new AccessException("Field: eventDate. Error: должно содержать дату, которая еще не наступила. " +
                    "Value: " + eventDto.getEventDate());
        } else {
            newEvent.setInitiator(user);
            newEvent.setCategory(category);
            newEvent.setCreatedOn(LocalDateTime.now());
            return mapper.mapToEventDto(events.save(newEvent));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ListEventShortDto getPrivateUserEvents(Long userId, Pageable pageable) {
        if (users.existsById(userId)) {
            return ListEventShortDto
                    .builder()
                    .events(mapper.mapToListEventShortDto(events.findAllByInitiatorUserId(userId, pageable)))
                    .build();
        } else {
            throw new NotFoundException("Пользователь с id=" + userId + " не найден");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public EventDto getPrivateUserEvent(Long userId, Long eventId) {
        if (users.existsById(userId)) {
            return mapper.mapToEventDto(events.findByEventIdAndInitiatorUserId(eventId, userId)
                    .orElseThrow(() -> new NotFoundException("Событие с id=" + eventId + " не найдено")));
        } else {
            throw new NotFoundException("Пользователь с id=" + userId + " не найден");
        }
    }

    @Override
    @Transactional
    public EventDto updateEventUser(Long userId, Long eventId, UpdateEventUserRequest updateEvent) {
        if (users.existsById(userId)) {
            LocalDateTime eventTime;
            if (updateEvent.getEventDate() != null) {
                eventTime = LocalDateTime.parse(updateEvent.getEventDate(), formatter);
                if (eventTime.isBefore(LocalDateTime.now().minusHours(2))) {
                    throw new AccessException("Field: eventDate. Error: должно содержать дату, которая еще не наступила. " +
                            "Value: " + eventTime);
                }
            }
            Event event = events.findByEventIdAndInitiatorUserId(eventId, userId)
                    .orElseThrow(() -> new NotFoundException("Событие с id=" + eventId + " не найдено"));
            if (event.getState().equals(State.PUBLISHED)) {
                throw new AccessException("Могут быть изменены только ожидающие или отмененные события");
            }
            if (updateEvent.getCategory() != null) {
                event.setCategory(categories.findById(updateEvent.getCategory()).orElseThrow(
                        () -> new NotFoundException("Категория с id=" + updateEvent.getCategory() + " не найдена")));
            }
            event.setState(StateAction.getState(updateEvent.getStateAction()));
            return mapper.mapToEventDto(events.save(mapper.mapToEvent(updateEvent, event)));
        } else {
            throw new NotFoundException("Пользователь с id=" + userId + " не найден");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ListEventDto getEventsByFiltersForAdmin(List<Long> ids, List<String> states, List<Long> categories,
                                                   LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                   Pageable pageable) {
        BooleanBuilder booleanBuilder = createQuery(ids, states, categories, rangeStart, rangeEnd);
        Page<Event> page;
        if (booleanBuilder.getValue() != null) {
            page = events.findAll(booleanBuilder, pageable);
        } else {
            page = events.findAll(pageable);
        }
        return ListEventDto
                .builder()
                .events(mapper.mapToListEventDto(page.getContent()))
                .build();
    }

    @Override
    @Transactional
    public EventDto updateEventAdmin(Long eventId, UpdateEventAdminRequest updateEvent) {
        LocalDateTime eventTime;
        if (updateEvent.getEventDate() != null) {
            eventTime = LocalDateTime.parse(updateEvent.getEventDate(), formatter);
            if (eventTime.isBefore(LocalDateTime.now().minusHours(1))) {
                throw new AccessException("Field: eventDate. Error: должно содержать дату, которая еще не наступила. " +
                        "Value: " + eventTime);
            }
        }
        Event event = events.findById(eventId).orElseThrow(
                () -> new NotFoundException("Событие с id=" + eventId + " не найдено"));
        changeEventState(event, updateEvent.getStateAction());
        if (updateEvent.getCategory() != null) {
            event.setCategory(categories.findById(updateEvent.getCategory()).orElseThrow(
                    () -> new NotFoundException("Категория с id=" + event.getCategory() + " не найдена")));
        }
        return mapper.mapToEventDto(events.save(mapper.mapToEvent(updateEvent, event)));
    }

    @Override
    @Transactional(readOnly = true)
    public RequestListDto getUserEventRequests(Long userId, Long eventId) {
        if (users.existsById(userId)) {
            Event event = events.findByEventIdAndInitiatorUserId(eventId, userId)
                    .orElseThrow(() -> new NotFoundException("Событие с id=" + eventId + " не найдено"));
            return RequestListDto
                    .builder()
                    .requests(event.getRequests().stream().map(requestMapper::mapToRequestDto)
                            .collect(Collectors.toList()))
                    .build();
        } else {
            throw new NotFoundException("Пользователь с id=" + userId + " не найден");
        }
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult approveRequests(Long userId, Long eventId, EventRequestStatusUpdate requests) {
        if (users.existsById(userId)) {
            Event event = events.findByEventIdAndInitiatorUserId(eventId, userId)
                    .orElseThrow(() -> new NotFoundException("Событие с id=" + eventId + " не найдено"));
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
        } else {
            throw new NotFoundException("Пользователь с id=" + userId + " не найден");
        }
    }

    @Override
    @Transactional
    public EventDto getEventByIdPublic(Long eventId, HttpServletRequest servlet) {
        statisticClient.postStats(servlet, "ewm-server");
        Event event = events.findByEventIdAndState(eventId, State.PUBLISHED)
                .orElseThrow(() -> new NotFoundException("Событие с id=" + eventId + " не найдено"));
        event.setViews(statisticClient.getViews(eventId));
        return mapper.mapToEventDto(events.save(event));
    }

    @Override
    @Transactional(readOnly = true)
    public ListEventShortDto getEventsByFiltersPublic(String text, List<Long> categories, Boolean paid,
                                                      LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                      Boolean onlyAvailable,
                                                      Pageable pageable, HttpServletRequest servlet) {
        statisticClient.postStats(servlet, "ewm-server");
        BooleanBuilder booleanBuilder = createQuery(null, null, categories, rangeStart, rangeEnd);
        Page<Event> page;
        if (text != null) {
            booleanBuilder.and(QEvent.event.annotation.likeIgnoreCase(text))
                    .or(QEvent.event.description.likeIgnoreCase(text));
        }
        if (rangeStart == null && rangeEnd == null) {
            booleanBuilder.and(QEvent.event.eventDate.after(LocalDateTime.now()));
        }
        if (onlyAvailable) {
            booleanBuilder.and((QEvent.event.participantLimit.eq(0)))
                    .or(QEvent.event.participantLimit.gt(QEvent.event.confirmedRequests));
        }
        if (paid != null) {
            booleanBuilder.and(QEvent.event.paid.eq(paid));
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
            booleanBuilder.and(QEvent.event.initiator.userId.in(ids));
        }
        if (states != null && !states.isEmpty()) {
            try {
                booleanBuilder.and(QEvent.event.state.in(states.stream().map(State::valueOf).collect(Collectors.toList())));
            } catch (Exception e) {
                log.info(e.getMessage());
            }
        }
        if (categories != null && !categories.isEmpty()) {
            booleanBuilder.and(QEvent.event.category.categoryId.in(categories));
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
        requestRepository.findAllByRequestIdIn(requests.getRequestIds()).stream().peek(r -> {
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
}
