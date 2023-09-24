package com.thedaymarket.controllers.response;

import com.thedaymarket.domain.UserAddress;

import java.math.BigDecimal;

public record AuthResponse(
    Long id,
    String accessToken,
    String role,
    Long expiry,
    String firstName,
    String lastName,
    String email,
    String profileImage,
    BigDecimal points,
    UserAddress address,
    boolean isFirstLogin) {}
