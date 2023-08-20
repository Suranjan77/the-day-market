package com.thedaymarket.utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public final class ExceptionUtils {
  private ExceptionUtils() {}

  public static ResponseStatusException getNotFoundExceptionResponse(String message) {
    return new ResponseStatusException(HttpStatus.NOT_FOUND, message);
  }

  public static ResponseStatusException getBadRequestExceptionResponse(String message) {
    return new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
  }
}
