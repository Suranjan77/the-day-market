package com.thedaymarket.service.impl;

import com.thedaymarket.controllers.request.CreateCategoryRequest;
import com.thedaymarket.domain.Category;
import com.thedaymarket.repository.CategoryRepository;
import com.thedaymarket.service.CategoryService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {

  private final CategoryRepository categoryRepository;

  @Override
  public Category getCategory(Long categoryId) {
    return null;
  }

  @Override
  public List<Category> getCategories(PageRequest pageRequest) {
    return categoryRepository.findAll(pageRequest).stream().toList();
  }

  @Override
  public List<Category> searchCategories(String query, int limit) {
    return categoryRepository.findByTagContainingIgnoreCase(
        query.toLowerCase(), PageRequest.of(0, limit));
  }

  @Override
  public Category getOrCreate(CreateCategoryRequest request) {
    var category = categoryRepository.getCategoriesByTag(request.tag()).orElse(new Category());
    category.setTag(request.tag().toLowerCase());
    return categoryRepository.save(category);
  }
}
