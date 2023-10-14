package com.thedaymarket.controllers.response;

import com.thedaymarket.domain.DutchAuctionState;
import java.math.BigDecimal;

public record DutchAuctionStateResponse(
    Long timerSeconds, BigDecimal currentPoints, Long auctionId, Boolean expired) {
  public static DutchAuctionStateResponse of(DutchAuctionState state) {
    return new DutchAuctionStateResponse(
        state.getTimerSeconds() == null ? 0L : state.getTimerSeconds(),
        state.getCurrentPoints(),
        state.getAuction().getId(),
        state.isExpired());
  }
}
