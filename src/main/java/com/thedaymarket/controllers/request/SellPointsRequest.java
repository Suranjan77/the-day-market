package com.thedaymarket.controllers.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record SellPointsRequest(
    @NotNull BankAccountDetails bankAccountDetails, @Positive BigDecimal points) {}
