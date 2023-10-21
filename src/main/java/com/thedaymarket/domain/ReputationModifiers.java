package com.thedaymarket.domain;

import lombok.Getter;

/** Modifiers total sum needs to be 1 */
@Getter
public enum ReputationModifiers {
  AUCTION_RATING(0.4f),
  SELLER_RATING(0.3f),
  LISTINGS(0.3f);

  private final float weight;

  ReputationModifiers(float weight) {
    this.weight = weight;
  }
}
