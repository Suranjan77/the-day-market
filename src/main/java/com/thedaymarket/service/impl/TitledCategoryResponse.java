package com.thedaymarket.service.impl;

import com.thedaymarket.controllers.response.CategoriesResponse;

import java.util.List;

public record TitledCategoryResponse(
    String title, String fieldName, List<CategoriesResponse> categories) {}
