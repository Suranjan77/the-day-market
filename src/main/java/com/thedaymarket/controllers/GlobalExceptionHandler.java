package com.thedaymarket.controllers;

import com.thedaymarket.controllers.response.ErrorMessage;
import com.thedaymarket.utils.ExceptionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(ExceptionUtils.BusinessException.class)
  public ResponseEntity<ErrorMessage> handleBusinessException(
      ExceptionUtils.BusinessException e, WebRequest request) {
    log.error("Error: ", e);
    return ResponseEntity.status(e.getStatusCode())
        .body(
            new ErrorMessage(
                e.getStatusCode().value(),
                e.getMessage(),
                null,
                request.getDescription(false),
                LocalDateTime.now()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorMessage> handleValidationException(
      MethodArgumentNotValidException e, WebRequest request) {
    log.error("Error: ", e);
    var errors =
        e.getBindingResult().getFieldErrors().stream()
            .map(err -> err.getField() + " : " + err.getDefaultMessage())
            .collect(Collectors.toList());

    return ResponseEntity.status(e.getStatusCode())
        .body(
            new ErrorMessage(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Validation error",
                errors,
                request.getDescription(false),
                LocalDateTime.now()));
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorMessage globalExceptionHandler(Exception ex, WebRequest request) {
    log.error("Error: ", ex);
    return new ErrorMessage(
        HttpStatus.INTERNAL_SERVER_ERROR.value(),
        ex.getMessage(),
        null,
        request.getDescription(false),
        LocalDateTime.now());
  }
}
