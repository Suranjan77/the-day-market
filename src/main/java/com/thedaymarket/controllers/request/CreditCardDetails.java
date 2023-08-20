package com.thedaymarket.controllers.request;

public record CreditCardDetails(
    String cardNumber, String cvv, String expiryDate, String nameOnCard) {}
