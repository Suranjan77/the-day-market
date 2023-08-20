package com.thedaymarket.controllers.request;

import java.math.BigDecimal;

public record BidRequest(BigDecimal amount, Long bidderId) {}
