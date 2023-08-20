package com.thedaymarket.controllers.response;

import java.math.BigDecimal;

public record PointsTransactionResponse(Long id, String buyer, String seller, BigDecimal amount) {}
