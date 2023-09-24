package com.thedaymarket.controllers.response;

import java.util.List;

public record PagedResponse<T>(
    List<T> data, int currentPage, int pageSize, int totalPages, long totalSize) {}
