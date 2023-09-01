package com.thedaymarket.controllers.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserRegisterRequest(
    @NotNull String firstName,
    @NotNull String lastName,
    @NotNull @Email String email,
    @NotNull @Size(min = 8, message = "Minimum 8 characters required for password")
        String password) {}
