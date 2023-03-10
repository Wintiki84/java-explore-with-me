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
import ru.practicum.validator.Details;

import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/comments")
@Validated
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PublicCommentController {
    private final CommentService commentService;

    @JsonView({Details.class})
    @GetMapping
    public ResponseEntity<CommentDtoList> getCommentsPublic(@RequestParam @Positive Long eventId) {
        log.info("получать комментарии для eventId={}", eventId);
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getCommentsPublic(eventId));
    }

    @JsonView({Details.class})
    @GetMapping("{commentId}")
    public ResponseEntity<CommentDto> getCommentPublic(@PathVariable @Positive Long commentId) {
        log.info("получать комментарий с id={}", commentId);
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getCommentPublic(commentId));
    }
}
