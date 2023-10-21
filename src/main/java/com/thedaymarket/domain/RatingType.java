package com.thedaymarket.domain;

import lombok.Getter;

@Getter
public enum RatingType {
  SELLER(User.class),
  BUYER(User.class),
  AUCTION(Auction.class);

  private final Class<?> domainType;

  RatingType(Class<?> domainType) {
    this.domainType = domainType;
  }
}
