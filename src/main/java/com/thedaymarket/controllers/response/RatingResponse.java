package com.thedaymarket.controllers.response;

import com.thedaymarket.domain.Rating;

public record RatingResponse(Long id, Integer stars) {
  public static RatingResponse of(Rating rating) {
    return new RatingResponse(rating.getId(), rating.getStars().intValue());
  }
}
