package com.thedaymarket.utils;

import com.thedaymarket.controllers.response.PagedResponse;
import com.thedaymarket.controllers.response.PagedResponseWithMax;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.function.ServerRequest;

@Slf4j
public final class RESTUtils {
  private RESTUtils() {}

  public static String getPathVariable(ServerRequest req, String pathVariableName) {
    var value = req.pathVariable(pathVariableName);
    if (!StringUtils.hasLength(value)) {
      throw ExceptionUtils.getBadRequestExceptionResponse(
          "Required path variable " + pathVariableName + " is missing");
    }
    return value;
  }

  public static PageRequest getPageRequest(Integer page, Integer size) {
    page = page == null ? 1 : Math.max(0, page - 1);
    size = size == null ? 50 : size;
    return PageRequest.of(page, size);
  }

  public static PageRequest getPageRequest(Integer page, Integer size, String sort) {
    if (sort != null && !sort.isBlank()) {
      return getPageRequest(page, size).withSort(Sort.by(sort.split(",")));
    } else {
      return getPageRequest(page, size);
    }
  }

  public static <T> PagedResponse<T> getPagedResponse(Page<T> pagedData) {
    return new PagedResponse<>(
        pagedData.getContent(),
        pagedData.getNumber(),
        pagedData.getSize(),
        pagedData.getTotalPages(),
        pagedData.getTotalElements());
  }

  public static <T> PagedResponseWithMax<T> getPagedResponseWithMax(T max, Page<T> pagedData) {
    return new PagedResponseWithMax<>(
        max,
        pagedData.getContent(),
        pagedData.getNumber(),
        pagedData.getSize(),
        pagedData.getTotalPages(),
        pagedData.getTotalElements());
  }
}
