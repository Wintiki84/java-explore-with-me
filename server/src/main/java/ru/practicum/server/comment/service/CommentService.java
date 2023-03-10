package ru.practicum.server.comment.service;

import ru.practicum.server.comment.dto.CommentDtoList;
import ru.practicum.server.comment.dto.CommentDto;

import javax.validation.constraints.NotNull;

public interface CommentService {
    @NotNull
    CommentDto addComment(@NotNull Long userId, @NotNull Long eventId, @NotNull CommentDto newComment);

    @NotNull
    CommentDto updateCommentUser(@NotNull Long userId, @NotNull Long commentId,
                                         @NotNull CommentDto updateComment);

    void deleteCommentUser(@NotNull Long commentId, @NotNull Long userId);

    void reportComment(@NotNull Long commentId, @NotNull Long userId);

    void deleteCommentAdmin(@NotNull Long commentId, @NotNull Long userId);

    @NotNull
    CommentDto updateCommentAdmin(@NotNull Long userId, @NotNull Long commentId,
                                          @NotNull CommentDto updateComment);

    @NotNull
    CommentDto getCommentPrivate(@NotNull Long userId, @NotNull Long commentId);

    @NotNull
    CommentDtoList getCommentsPrivate(@NotNull Long userId, @NotNull Long eventId);

    @NotNull
    CommentDtoList getCommentsPublic(@NotNull Long eventId);

    @NotNull
    CommentDto getCommentPublic(@NotNull Long commentId);
}
