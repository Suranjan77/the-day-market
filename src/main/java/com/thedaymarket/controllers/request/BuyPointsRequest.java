package com.thedaymarket.controllers.request;

import java.math.BigDecimal;

public record BuyPointsRequest(CreditCardDetails creditCardDetails, BigDecimal amount) {}
