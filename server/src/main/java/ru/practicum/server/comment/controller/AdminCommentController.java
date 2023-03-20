package ru.practicum.server.comment.controller;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.comment.dto.CommentDto;
import ru.practicum.server.comment.service.CommentService;
import ru.practicum.validator.AdminDetails;
import ru.practicum.validator.Update;

import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/admin/users")
@Validated
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdminCommentController {
    private final CommentService commentService;

    @JsonView(AdminDetails.class)
    @DeleteMapping("{userId}/comments/{commentId}")
    public ResponseEntity<Void> deleteCommentAdmin(@PathVariable @Positive Long userId,
                                                   @PathVariable @Positive Long commentId) {
        log.info("администратор удаляет комментарий с userId={} и commentId={}", userId, commentId);
        commentService.deleteCommentAdmin(commentId, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @JsonView(AdminDetails.class)
    @PatchMapping("{userId}/comments/{commentId}")
    public ResponseEntity<CommentDto> updateCommentAdmin(@PathVariable @Positive Long userId,
                                                                 @PathVariable @Positive Long commentId,
                                                                 @RequestBody @Validated(Update.class)
                                                                     CommentDto commentDto) {
        log.info("обновить комментарий commentId={} и userId={}: {}", commentId, userId, commentDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(commentService.updateCommentAdmin(userId, commentId, commentDto));
    }

}
