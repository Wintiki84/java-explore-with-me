package ru.practicum.server.event.mapper;

import org.mapstruct.*;
import ru.practicum.server.category.mapper.CategoryMapper;
import ru.practicum.server.comment.mapper.CommentMapper;
import ru.practicum.server.event.dto.*;
import ru.practicum.server.event.model.Event;
import ru.practicum.server.user.mapper.UserMapper;

import java.util.List;

import static ru.practicum.constants.Constants.DATE_FORMAT;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class, UserMapper.class, CommentMapper.class})
public interface EventMapper {

    @Mapping(target = "category", ignore = true)
    @Mapping(source = "eventDate", target = "eventDate", dateFormat = DATE_FORMAT)
    Event mapToEvent(EventDtoRequest eventDtoRequest);

    @Mapping(source = "eventDate", target = "eventDate", dateFormat = DATE_FORMAT)
    @Mapping(source = "createdOn", target = "createdOn", dateFormat = DATE_FORMAT)
    @Mapping(source = "publishedOn", target = "publishedOn", dateFormat = DATE_FORMAT)
    EventDtoResponse mapToEventDtoResponse(Event event);

    @Mapping(source = "eventDate", target = "eventDate", dateFormat = DATE_FORMAT)
    EventShortDto mapToEventShortDto(Event event);

    List<EventShortDto> mapToListEventShortDto(List<Event> events);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "category", ignore = true)
    @Mapping(source = "eventDate", target = "eventDate", dateFormat = DATE_FORMAT)
    Event mapToEvent(EventDtoRequest updateEvent, @MappingTarget Event event);

    List<EventDtoResponse> mapToListEventDtoResponse(List<Event> events);
}