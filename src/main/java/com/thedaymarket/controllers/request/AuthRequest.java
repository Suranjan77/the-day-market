package com.thedaymarket.controllers.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record AuthRequest(
    @NotNull @Email(message = "Invalid email") String email, @NotNull String password) {}
