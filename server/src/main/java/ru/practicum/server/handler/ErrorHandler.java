package ru.practicum.server.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.practicum.server.handler.exception.AccessException;
import ru.practicum.server.handler.exception.EventStateException;
import ru.practicum.server.handler.exception.EventUpdateException;
import ru.practicum.server.handler.exception.NotFoundException;
import ru.practicum.server.handler.response.ApiError;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@RestControllerAdvice("ru.practicum.server")
@Slf4j
public class ErrorHandler {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<ApiError> handleException(MethodArgumentNotValidException e) {
        ApiError errorResponse = ApiError.builder()
                .errors(Arrays.asList(e.getStackTrace()))
                .message(e.getFieldError().getDefaultMessage())
                .reason("Неправильный запрос.")
                .status(HttpStatus.BAD_REQUEST.toString())
                .timestamp(LocalDateTime.now().format(formatter))
                .build();
        log.info("HttpStatus.BAD_REQUEST:{}", errorResponse);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    private ResponseEntity<ApiError> handleException(DataIntegrityViolationException e) {
        ApiError errorResponse = ApiError.builder()
                .errors(Arrays.asList(e.getStackTrace()))
                .message(e.getMessage())
                .reason("Некорректная валидация.")
                .status(String.valueOf(HttpStatus.CONFLICT))
                .timestamp(LocalDateTime.now().format(formatter))
                .build();
        log.info("HttpStatus.CONFLICT:{}", errorResponse);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(NotFoundException.class)
    private ResponseEntity<ApiError> handleException(NotFoundException e) {
        ApiError errorResponse = ApiError.builder()
                .errors(Arrays.asList(e.getStackTrace()))
                .message(e.getMessage())
                .reason("Объект не найден.")
                .status(HttpStatus.NOT_FOUND.toString())
                .timestamp(LocalDateTime.now().format(formatter))
                .build();
        log.info("HttpStatus.NOT_FOUND:{}", errorResponse);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    private ResponseEntity<ApiError> handleException(ConstraintViolationException e) {
        ApiError errorResponse = ApiError.builder()
                .errors(Arrays.asList(e.getStackTrace()))
                .message(e.getMessage())
                .reason("Недопустимые параметры запросаs.")
                .status(HttpStatus.BAD_REQUEST.toString())
                .timestamp(LocalDateTime.now().format(formatter))
                .build();
        log.info("HttpStatus.BAD_REQUEST:{}", errorResponse);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler({AccessException.class, EventStateException.class})
    private ResponseEntity<ApiError> handleExceptions(RuntimeException e) {
        ApiError errorResponse = ApiError.builder()
                .errors(Arrays.asList(e.getStackTrace()))
                .message(e.getMessage())
                .reason("Для запрошенной операции условия не соблюдены.")
                .status(HttpStatus.CONFLICT.toString())
                .timestamp(LocalDateTime.now().format(formatter))
                .build();
        log.info("HttpStatus.CONFLICT:{}", errorResponse);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class, EventUpdateException.class,
            MissingServletRequestParameterException.class})
    private ResponseEntity<ApiError> handleException(Exception e) {
        ApiError errorResponse = ApiError.builder()
                .errors(Arrays.asList(e.getStackTrace()))
                .message(e.getMessage())
                .reason("Неправильно сделанный запрос.")
                .status(HttpStatus.BAD_REQUEST.toString())
                .timestamp(LocalDateTime.now().format(formatter))
                .build();
        log.info("HttpStatus.BAD_REQUEST:{}", errorResponse);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
