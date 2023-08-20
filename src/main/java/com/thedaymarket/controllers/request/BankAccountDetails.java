package com.thedaymarket.controllers.request;

import jakarta.validation.constraints.NotNull;

public record BankAccountDetails(
    @NotNull String accountName, @NotNull String accountNumber, @NotNull String sortCode) {}
