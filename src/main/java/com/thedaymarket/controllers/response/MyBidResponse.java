package com.thedaymarket.controllers.response;

import java.math.BigDecimal;

public record MyBidResponse(
    Long id,
    String image,
    Long auctionId,
    String title,
    String description,
    BigDecimal bidAmount,
    BigDecimal winningBidAmount,
    Float stars,
    MyBidStatus status) {}
