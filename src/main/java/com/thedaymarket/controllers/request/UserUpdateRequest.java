package com.thedaymarket.controllers.request;

import com.thedaymarket.domain.UserAddress;
import com.thedaymarket.domain.UserRole;

public record UserUpdateRequest(
    String firstName,
    String lastName,
    String password,
    UserRole role,
    UserAddress address,
    String desc) {}
