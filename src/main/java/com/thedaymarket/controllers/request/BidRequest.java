package com.thedaymarket.controllers.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record BidRequest(@NotNull @Positive BigDecimal amount, @NotNull Long bidderId) {}
