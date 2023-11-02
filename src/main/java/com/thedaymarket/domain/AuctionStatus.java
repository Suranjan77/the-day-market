package com.thedaymarket.domain;

/**
 * AuctionStatus SOLD -- bought by buyer, CLOSED -- closed at the auction end and has a winner,
 * SCHEDULED -- to be published, PUBLISHED -- published today UNSOLD -- No bidders or winner
 * rejected, DRAFT -- Not yet ready
 */
public enum AuctionStatus {
  SOLD,
  CLOSED,
  SCHEDULED,
  PUBLISHED,
  UNSOLD,
  DRAFT,
  NONE
}
