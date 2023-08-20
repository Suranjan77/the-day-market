package com.thedaymarket.utils;

import com.thedaymarket.controllers.response.PagedResponse;
import jakarta.servlet.ServletException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.StringUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.server.ServerWebInputException;
import org.springframework.web.servlet.function.ServerRequest;

import java.io.IOException;
import java.util.List;

public final class RESTUtils {
  private RESTUtils() {}

  public static <T> T parseRequestBody(ServerRequest request, Class<T> type, Validator validator) {
    try {
      var body = request.body(type);
      validate(body, validator);
      return body;
    } catch (ServletException | IOException e) {
      throw ExceptionUtils.getBadRequestExceptionResponse(
          "Failed to parse request body: " + e.getMessage());
    }
  }

  public static String getPathVariable(ServerRequest req, String pathVariableName) {
    var value = req.pathVariable(pathVariableName);
    if (!StringUtils.hasLength(value)) {
      throw ExceptionUtils.getBadRequestExceptionResponse(
          "Required path variable " + pathVariableName + " is missing");
    }
    return value;
  }

  public static String getRequiredParameter(ServerRequest req, String paramName) {
    var value = req.param(paramName);
    if (value.isEmpty()) {
      throw ExceptionUtils.getBadRequestExceptionResponse(
          "Required parameter " + paramName + " is missing");
    }
    return value.get();
  }

  public static PageRequest getPageRequest(ServerRequest request) {
    var page = request.param("page").map(Integer::parseInt).orElse(1);
    var size = request.param("size").map(Integer::parseInt).orElse(50);
    return PageRequest.of(page, size);
  }

  public static <T> PagedResponse<List<T>> getPagedResponse(Page<T> pagedData) {
    return new PagedResponse<>(
        pagedData.getContent(),
        pagedData.getNumber(),
        pagedData.getSize(),
        pagedData.getTotalPages(),
        pagedData.getTotalElements());
  }

  private static <T> void validate(T data, Validator validator) {
    var errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());
    validator.validate(data, errors);
    if (errors.hasErrors()) {
      throw ExceptionUtils.getBadRequestExceptionResponse(errors.toString());
    }
  }
}
