package com.thedaymarket.controllers.response;

public record StreamResponse<T>(StreamStatus status, T data) {}
