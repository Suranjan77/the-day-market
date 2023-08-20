package com.thedaymarket.service.impl;

import com.thedaymarket.domain.PaymentMethod;

import java.math.BigDecimal;

public record TransactionResponse(
        int statusCode, boolean success, String referenceId, BigDecimal amount, PaymentMethod paymentMethod) {}
