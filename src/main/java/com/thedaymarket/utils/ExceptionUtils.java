package com.thedaymarket.utils;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public final class ExceptionUtils {
  private ExceptionUtils() {}

  @Getter
  public static class BusinessException extends RuntimeException {
    private final HttpStatusCode statusCode;

    public BusinessException(HttpStatusCode statusCode, String message) {
      super(message);
      this.statusCode = statusCode;
    }
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
    return new BusinessException(HttpStatus.FORBIDDEN, message);
  }
}
