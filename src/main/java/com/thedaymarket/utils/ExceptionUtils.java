package com.thedaymarket.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public final class ExceptionUtils {
  private ExceptionUtils() {}

  @Getter
  @AllArgsConstructor
  public static class BusinessException extends RuntimeException {
    private final HttpStatusCode statusCode;
    private final String message;
  }

  public static BusinessException getNotFoundExceptionResponse(String message) {
    return new BusinessException(HttpStatus.NOT_FOUND, message);
  }

  public static BusinessException getBadRequestExceptionResponse(String message) {
    return new BusinessException(HttpStatus.BAD_REQUEST, message);
  }

  public static BusinessException getServerException(String message) {
    return new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, message);
  }

  public static BusinessException getAuthenticationException(String message) {
    return new BusinessException(HttpStatus.UNAUTHORIZED, message);
  }
}
