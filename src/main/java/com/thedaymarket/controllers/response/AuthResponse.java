package com.thedaymarket.controllers.response;

public record AuthResponse(
    Long id,
    String accessToken,
    String role,
    Long expiry,
    String firstName,
    String lastName,
    String email,
    String profileImage,
    boolean isFirstLogin) {}
