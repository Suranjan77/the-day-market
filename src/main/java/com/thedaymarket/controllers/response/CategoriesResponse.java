package com.thedaymarket.controllers.response;

import com.thedaymarket.domain.Category;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public record CategoriesResponse(
    Long id, String tag, CategoriesResponse parent, List<CategoriesResponse> children) {
  public static CategoriesResponse of(Category category) {
    return new CategoriesResponse(
        category.getId(),
        category.getTag(),
        category.getParent() != null ? of(category.getParent()) : null,
        category.getChildren() != null
            ? category.getChildren().stream()
                .map(CategoriesResponse::of)
                .collect(Collectors.toList())
            : Collections.EMPTY_LIST);
  }
}
