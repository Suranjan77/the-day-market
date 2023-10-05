package com.thedaymarket.controllers.response;

import com.thedaymarket.domain.DutchAuctionState;
import java.math.BigDecimal;
import java.time.LocalTime;

public record DutchAuctionStateResponse(
    LocalTime currentTime, BigDecimal currentPoints, Long auctionId, Boolean expired) {
  public static DutchAuctionStateResponse of(DutchAuctionState state) {
    return new DutchAuctionStateResponse(
        LocalTime.now(), state.getCurrentPoints(), state.getAuction().getId(), state.isExpired());
  }
}
