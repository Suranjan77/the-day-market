package com.thedaymarket.controllers.controller;

import com.thedaymarket.controllers.request.CreateCategoryRequest;
import com.thedaymarket.controllers.response.CategoriesResponse;
import com.thedaymarket.controllers.response.Suggestion;
import com.thedaymarket.domain.Category;
import com.thedaymarket.service.CategoryService;
import com.thedaymarket.utils.RESTUtils;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/categories")
public class CategoriesController {

  private final CategoryService categoryService;

  @GetMapping()
  public List<CategoriesResponse> getCategories() {
    var pageRequest = RESTUtils.getPageRequest(0, 20);
    var categories = categoryService.getCategories(pageRequest);
    return categories.stream().map(CategoriesResponse::of).collect(Collectors.toList());
  }

  @GetMapping("suggestions")
  public List<Suggestion> getCategorySuggestions(@RequestParam("query") String query) {
    var categories = categoryService.searchCategories(query, 20);
    return categories.stream().map(Suggestion::ofCategory).collect(Collectors.toList());
  }

  @PostMapping()
  public Category createCategory(@RequestBody CreateCategoryRequest categoryRequest) {
    return categoryService.getOrCreate(categoryRequest);
  }

  public ServerResponse createSubCategory(ServerRequest req) {
    return ServerResponse.ok().build();
  }

  public ServerResponse getCategories(ServerRequest req) {
    return ServerResponse.ok().build();
  }

  public ServerResponse getCategory(ServerRequest req) {
    //      @PathVariable("categoryId") Long categoryId
    return ServerResponse.ok().build();
  }

  public ServerResponse getChildrenCategories(ServerRequest req) {
    //      @PathVariable("categoryId") Long categoryId
    return ServerResponse.ok().build();
  }

  public ServerResponse deleteCategory(ServerRequest req) {
    //      @PathVariable("categoryId") Long categoryId
    return ServerResponse.ok().build();
  }
}
