package com.thedaymarket.controllers.response;

public record AuthResponse(String accessToken, String role, Long expiry) {}
