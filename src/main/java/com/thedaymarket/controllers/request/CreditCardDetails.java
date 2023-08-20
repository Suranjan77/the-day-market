package com.thedaymarket.controllers.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

public record CreditCardDetails(
    @NotNull String cardNumber,
    @NotNull @Digits(integer = 3, fraction = 0) String cvv,
    @Future String expiryDate,
    @NotNull String nameOnCard) {}
