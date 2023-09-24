package com.thedaymarket.controllers.response;

import java.util.List;

public record PagedResponseWithMax<T>(
    T max, List<T> data, int currentPage, int pageSize, int totalPages, long totalSize) {}
