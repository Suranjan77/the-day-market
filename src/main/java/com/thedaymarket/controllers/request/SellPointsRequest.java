package com.thedaymarket.controllers.request;


import java.math.BigDecimal;

public record SellPointsRequest(BankAccountDetails creditCardDetails, BigDecimal points) {}
