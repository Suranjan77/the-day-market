package com.thedaymarket.controllers.response;

import com.thedaymarket.domain.Auction;
import com.thedaymarket.domain.AuctionStatus;
import com.thedaymarket.domain.AuctionType;

import java.math.BigDecimal;

public record AuctionShortResponse(
    Long id, String title, BigDecimal minAskPrice, AuctionType type, String imageName, AuctionStatus status) {
  public static AuctionShortResponse fromAuction(Auction auction) {
    return new AuctionShortResponse(
        auction.getId(),
        auction.getTitle(),
        auction.getMinAskPrice(),
        auction.getType(),
        auction.getImageName(),
        auction.getStatus());
  }
}
