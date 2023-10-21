package com.thedaymarket.controllers.response;

import lombok.Getter;

import java.util.function.Predicate;

@Getter
public enum ReputationStatus {
  HIGH(p -> p >= 85.0),
  NEUTRAL(p -> p < 85.0);

  private final Predicate<Double> predicate;
  private final Double upperBound = 85.0;

  ReputationStatus(Predicate<Double> predicate) {
    this.predicate = predicate;
  }

  public ReputationStatus which(Double points) {
    if (HIGH.predicate.test(points)) {
      return HIGH;
    } else {
      return NEUTRAL;
    }
  }
}
