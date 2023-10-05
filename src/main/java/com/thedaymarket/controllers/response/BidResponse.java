package com.thedaymarket.controllers.response;

import com.thedaymarket.domain.Bid;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

public record BidResponse(Long id, LocalDateTime createdAt, BigDecimal amount, UserResponse user) {
  public static BidResponse of(Bid bid) {
    return BidResponse.of(bid, false);
  }

  public static BidResponse of(Bid bid, boolean hideAmount) {
    if (bid == null) {
      return new BidResponse(null, null, null, null);
    }
    return new BidResponse(
        bid.getId(),
        bid.getCreatedAt(),
        hideAmount ? null : bid.getAmount(),
        UserResponse.ofUser(bid.getUser()));
  }
}
