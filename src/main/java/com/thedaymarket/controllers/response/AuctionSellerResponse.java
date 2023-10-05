package com.thedaymarket.controllers.response;

import com.thedaymarket.domain.User;

public record AuctionSellerResponse(String name, ReputationStatus reputation) {
  public static AuctionSellerResponse of(User seller) {
    return new AuctionSellerResponse(
        seller.getFirstName(), ReputationStatus.NEUTRAL.which(seller.getReputationPoints()));
  }
}
