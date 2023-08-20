package com.thedaymarket.controllers.response;

public record PagedResponse<T>(
    T data, int currentPage, int pageSize, int totalPages, long totalSize) {}
