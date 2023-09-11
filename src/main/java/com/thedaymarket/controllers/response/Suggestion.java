package com.thedaymarket.controllers.response;

import com.thedaymarket.domain.Category;

public record Suggestion(String tag) {
  public static Suggestion ofCategory(Category category) {
    return new Suggestion(category.getTag());
  }
}
