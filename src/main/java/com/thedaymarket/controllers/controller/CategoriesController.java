package com.thedaymarket.controllers.controller;

import com.thedaymarket.controllers.request.CreateCategoryRequest;
import com.thedaymarket.controllers.response.CategoriesResponse;
import com.thedaymarket.controllers.response.Suggestion;
import com.thedaymarket.domain.AuctionStatus;
import com.thedaymarket.domain.Category;
import com.thedaymarket.service.CategoryService;
import com.thedaymarket.service.impl.TitledCategoryResponse;
import com.thedaymarket.utils.AuctionSearchFieldNames;
import com.thedaymarket.utils.RESTUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/categories")
public class CategoriesController {

  private final CategoryService categoryService;

  @GetMapping()
  public List<TitledCategoryResponse> getCategories() {
    var pageRequest = RESTUtils.getPageRequest(0, 20);
    var titledCategories = new ArrayList<TitledCategoryResponse>();

    var categories =
        categoryService.getCategories(pageRequest).stream().map(CategoriesResponse::of).toList();
    titledCategories.add(
        new TitledCategoryResponse("Categories", AuctionSearchFieldNames.CATEGORIES, categories));

    titledCategories.add(
        new TitledCategoryResponse(
            "Auction types",
            AuctionSearchFieldNames.AUCTION_TYPES,
            List.of(
                new CategoriesResponse("Dutch", null, null),
                new CategoriesResponse("English", null, null),
                new CategoriesResponse("Sealed", null, null))));

    return titledCategories;
  }

  @GetMapping("sort")
  public List<CategoriesResponse> getSort() {
    return List.of(
        new CategoriesResponse("High", null, null),
        new CategoriesResponse("Neutral", null, null),
        new CategoriesResponse("Low", null, null));
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
}
