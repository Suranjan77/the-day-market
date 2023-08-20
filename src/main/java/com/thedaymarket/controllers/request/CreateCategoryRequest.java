package com.thedaymarket.controllers.request;

import jakarta.validation.constraints.NotNull;

public record CreateCategoryRequest(@NotNull String tag) {}
