package ru.practicum.server.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.server.comment.dto.CommentDtoList;
import ru.practicum.server.comment.dto.CommentDto;
import ru.practicum.server.comment.enums.CommentState;
import ru.practicum.server.comment.mapper.CommentMapper;
import ru.practicum.server.comment.model.Comment;
import ru.practicum.server.comment.repository.CommentRepository;
import ru.practicum.server.event.enums.State;
import ru.practicum.server.event.model.Event;
import ru.practicum.server.event.repository.EventRepository;
import ru.practicum.server.handler.exception.AccessException;
import ru.practicum.server.handler.exception.CommentException;
import ru.practicum.server.handler.exception.NotFoundException;
import ru.practicum.server.report.model.Report;
import ru.practicum.server.report.repository.ReportRepository;
import ru.practicum.server.user.model.User;
import ru.practicum.server.user.repository.UserRepository;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CommentServiceImp implements CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper mapper;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final ReportRepository reportRepository;

    @Override
    @NotNull
    @Transactional
    public CommentDto addComment(@NotNull Long userId, @NotNull Long eventId,
                                         @NotNull CommentDto newComment) {
        User user = findByUserId(userId);
        Event event;
        if (user.getCommentsAreProhibited()) {
            throw new AccessException("У пользователя с id=" + userId + " заблокированы комментарии");
        }
        event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие с id=" + eventId + " не найдено"));
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new AccessException("Невозможно добавить комментарий к событию в статусе " + event.getState());
        }
        Comment comment = mapper.mapToComment(newComment);
        comment.setAuthor(user);
        comment.setEvent(event);
        comment.setCreated(LocalDateTime.now());
        comment.setState(CommentState.NOT_EDIT);
        return mapper.mapToCommentDto(commentRepository.save(comment));
    }

    @Override
    @NotNull
    @Transactional
    public CommentDto updateCommentUser(@NotNull Long userId, @NotNull Long commentId,
                                                @NotNull CommentDto updateComment) {
        Comment comment = findByCommentIdAndAuthorId(commentId, userId);
        if (LocalDateTime.now().isAfter(comment.getCreated().plusHours(1))) {
            throw new CommentException("Нельзя обновить комментарий, созданный более 1 часа назад");
        }
        comment.setState(CommentState.EDITED);
        return mapper.mapToCommentDto(commentRepository.save(mapper.mapToComment(updateComment, comment)));
    }

    @Override
    @Transactional
    public void deleteCommentUser(Long commentId, Long userId) {
        Comment comment = findByCommentIdAndAuthorId(commentId, userId);
        if (LocalDateTime.now().isAfter(comment.getCreated().plusHours(1))) {
            throw new CommentException("Нельзя удалить комментарий, созданный более 1 часа назад");
        }
        commentRepository.deleteById(commentId);
    }

    @Override
    @Transactional
    public void reportComment(@NotNull Long commentId, @NotNull Long userId) {
        Comment comment = findByCommentIdAndAuthorId(commentId, userId);
        Report report = new Report();
        report.setReportedUser(comment.getAuthor());
        report.setReportedMessage(comment.getText());
        reportRepository.save(report);
    }

    @Override
    @Transactional
    public void deleteCommentAdmin(@NotNull Long commentId, @NotNull Long userId) {
        findByCommentIdAndAuthorId(commentId, userId);
        commentRepository.deleteById(commentId);
    }

    @Override
    @NotNull
    @Transactional
    public CommentDto updateCommentAdmin(@NotNull Long userId, @NotNull Long commentId,
                                                 @NotNull CommentDto updateComment) {
        Comment comment = findByCommentIdAndAuthorId(commentId, userId);
        comment.setState(CommentState.EDITED);
        return mapper.mapToCommentDto(commentRepository.save(mapper.mapToComment(updateComment, comment)));
    }

    @Override
    @NotNull
    @Transactional(readOnly = true)
    public CommentDto getCommentPrivate(@NotNull Long userId, @NotNull Long commentId) {
        Comment comment = findByCommentIdAndAuthorId(commentId, userId);
        return mapper.mapToCommentDto(comment);
    }

    @Override
    @NotNull
    @Transactional(readOnly = true)
    public CommentDtoList getCommentsPrivate(@NotNull Long userId, @NotNull Long eventId) {
        return CommentDtoList
                .builder()
                .comments(commentRepository.findAllByAuthorIdAndEventId(userId, eventId).stream()
                        .map(mapper::mapToCommentShortDto).collect(Collectors.toList()))
                .build();
    }

    @Override
    @NotNull
    @Transactional(readOnly = true)
    public CommentDtoList getCommentsPublic(@NotNull Long eventId) {
        return CommentDtoList
                .builder()
                .comments(commentRepository.findAllByEventId(eventId).stream()
                        .map(mapper::mapToCommentShortDto).collect(Collectors.toList()))
                .build();
    }

    @Override
    @NotNull
    @Transactional(readOnly = true)
    public CommentDto getCommentPublic(@NotNull Long commentId) {
        return mapper.mapToCommentDto(findByCommentId(commentId));
    }

    private Comment findByCommentId(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Коммантарий с id=" + commentId + " не найден"));
    }

    private Comment findByCommentIdAndAuthorId(Long commentId, Long userId) {
        return commentRepository.findByIdAndAuthorId(commentId, userId)
                .orElseThrow(() -> new NotFoundException("Коммантарий с id=" + commentId + " пользователя с id=" +
                        userId + " не найден"));
    }

    private User findByUserId(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=" + userId + " не найден"));
    }
}
