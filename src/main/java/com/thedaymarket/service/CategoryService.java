package com.thedaymarket.service;

import com.thedaymarket.controllers.request.CreateCategoryRequest;
import com.thedaymarket.domain.Category;

import java.util.List;

public interface CategoryService {
  Category getCategory(Long categoryId);

  List<Category> searchCategories(String query, int limit);

  Category getOrCreate(CreateCategoryRequest request);
}
