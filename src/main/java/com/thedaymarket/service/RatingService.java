package com.thedaymarket.service;

import com.thedaymarket.domain.Rating;
import com.thedaymarket.domain.RatingType;
import com.thedaymarket.domain.User;

import java.util.Optional;

public interface RatingService {
  Rating addRating(Integer stars, RatingType type, User rater, Long receiverId);

  Optional<Rating> getRatingByRater(RatingType type, User rater, Long receiverId);

  int getAverageRating(RatingType type, Long receiverId);
}
