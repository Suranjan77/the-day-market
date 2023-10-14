package com.thedaymarket.service.impl;

import com.thedaymarket.domain.Rating;
import com.thedaymarket.domain.RatingType;
import com.thedaymarket.domain.User;
import com.thedaymarket.repository.RatingRepository;
import com.thedaymarket.service.RatingService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class RatingServiceImpl implements RatingService {

  private final RatingRepository ratingRepository;

  @Override
  public Rating addRating(Integer stars, RatingType type, User rater, Long receiverId) {
    var rating =
        ratingRepository
            .findByTypeAndRaterAndReceiverId(type, rater, receiverId)
            .orElse(new Rating());

    if (rating.getId() == null) {
      rating.setRater(rater);
      rating.setReceiverId(receiverId);
      rating.setType(type);
    }

    rating.setStars(stars.floatValue());

    return ratingRepository.save(rating);
  }

  @Override
  public Optional<Rating> getRatingByRater(RatingType type, User rater, Long receiverId) {
    return ratingRepository.findByTypeAndRaterAndReceiverId(type, rater, receiverId);
  }

  @Override
  public Float getAverageRating(RatingType type, Long receiverId) {
    var ratings = ratingRepository.findByTypeAndReceiverId(type, receiverId);

    if (!ratings.isEmpty()) {
      var totalRating = ratings.stream().map(Rating::getStars).reduce(0.0f, Float::sum);
      var averageRating = totalRating / ratings.size();
      return Math.min((float) Math.ceil(averageRating), 5.0f);
    }

    return 0.0f;
  }
}
