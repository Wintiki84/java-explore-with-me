package ru.practicum.server.comment.controller;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.comment.dto.CommentDtoList;
import ru.practicum.server.comment.dto.CommentDto;
import ru.practicum.server.comment.service.CommentService;
import ru.practicum.validator.Create;
import ru.practicum.validator.Private;
import ru.practicum.validator.Update;

import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/users")
@Validated
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PrivateCommentController {
    private final CommentService commentService;

    @JsonView({Private.class})
    @PostMapping("{userId}/comments")
    public ResponseEntity<CommentDto> addComment(@PathVariable @Positive Long userId,
                                     @RequestParam @Positive Long eventId,
                                     @RequestBody @Validated(Create.class) CommentDto comment) {
        log.info("добавить новый комментарий пользователя c id={} для события с id={}:{}", userId, eventId, comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.addComment(userId, eventId, comment));
    }

    @JsonView({Private.class})
    @PatchMapping("{userId}/comments/{commentId}")
    public ResponseEntity<CommentDto> updateCommentAdmin(@PathVariable @Positive Long userId,
                                                            @PathVariable @Positive Long commentId,
                                                            @RequestBody @Validated(Update.class)
                                                                     CommentDto updateComment) {
        log.info("обновить комментарий с id={} пользователем с id={}: {}", commentId, userId, updateComment);
        return ResponseEntity.status(HttpStatus.OK)
                .body(commentService.updateCommentUser(userId, commentId, updateComment));
    }

    @JsonView({Private.class})
    @DeleteMapping("{userId}/comments/{commentId}")
    public ResponseEntity<Void> deleteCommentUser(@PathVariable @Positive Long userId,
                                                  @PathVariable @Positive Long commentId) {
        log.info("удалить комментарий с id={} пользователем с id={}", commentId, userId);
        commentService.deleteCommentUser(commentId, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @JsonView({Private.class})
    @PatchMapping("comments")
    public ResponseEntity<Void> reportCommentUser(@RequestParam @Positive Long reportUserId,
                                                  @RequestParam @Positive Long commentId) {
        log.info("отчет userId={} для commentId={}", reportUserId, commentId);
        commentService.reportComment(commentId, reportUserId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @JsonView({Private.class})
    @GetMapping("{userId}/comments/{commentId}")
    public ResponseEntity<CommentDto> getCommentPrivate(@PathVariable @Positive Long userId,
                                                                @PathVariable @Positive Long commentId) {
        log.info("получить личный комментарий с userId={} и commentId={}", userId, commentId);
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getCommentPrivate(userId, commentId));
    }

    @JsonView({Private.class})
    @GetMapping("{userId}/events/{eventId}/comments")
    public ResponseEntity<CommentDtoList> getCommentsPrivate(@PathVariable @Positive Long userId,
                                                             @PathVariable @Positive Long eventId) {
        log.info("получить комментарий с userId={} и commentId={}", userId, eventId);
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getCommentsPrivate(userId, eventId));
    }
}
