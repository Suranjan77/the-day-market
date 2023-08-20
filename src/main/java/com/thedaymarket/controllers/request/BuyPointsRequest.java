package com.thedaymarket.controllers.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record BuyPointsRequest(
    @NotNull CreditCardDetails creditCardDetails, @NotNull @Positive BigDecimal amount) {}
