package com.thedaymarket.domain;

public enum AuctionStatus {
  SOLD, // A job picks up CLOSED auctions and determine winner and sets SOLD status, if no winner then UNSOLD
  CLOSED, //At the end of the market CLOSED
  SCHEDULED,
  PUBLISHED,
  UNSOLD,
  DRAFT,
  NONE
}
