package ru.practicum.server.comment.mapper;

import org.mapstruct.*;
import ru.practicum.server.comment.dto.CommentDto;
import ru.practicum.server.comment.dto.CommentShortDto;
import ru.practicum.server.comment.model.Comment;

import java.util.Set;

import static ru.practicum.constants.Constants.DATE_FORMAT;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    Comment mapToComment(CommentDto newComment);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Comment mapToComment(CommentDto updateComment, @MappingTarget Comment comment);

    @Mapping(source = "author.name", target = "authorName")
    @Mapping(source = "event.title", target = "eventTitle")
    @Mapping(source = "created", target = "created", dateFormat = DATE_FORMAT)
    CommentDto mapToCommentDto(Comment comment);

    @Mapping(source = "author.name", target = "authorName")
    @Mapping(source = "created", target = "created", dateFormat = DATE_FORMAT)
    CommentShortDto mapToCommentShortDto(Comment comment);

    Set<CommentShortDto> mapToSetCommentShort(Set<Comment> comments);
}
