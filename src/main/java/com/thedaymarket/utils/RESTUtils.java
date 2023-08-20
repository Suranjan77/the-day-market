package com.thedaymarket.utils;

import jakarta.servlet.ServletException;
import org.springframework.web.server.ServerWebInputException;
import org.springframework.web.servlet.function.ServerRequest;

import java.io.IOException;

public final class RESTUtils {
  private RESTUtils() {}

  public static  <T> T parseRequest(ServerRequest request, Class<T> type) {
    try {
      return request.body(type);
    } catch (ServletException | IOException e) {
      throw new ServerWebInputException("Failed to parse request body", null, e);
    }
  }
}
