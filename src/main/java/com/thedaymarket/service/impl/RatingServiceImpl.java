package com.thedaymarket.service.impl;

import com.thedaymarket.domain.Rating;
import com.thedaymarket.domain.RatingType;
import com.thedaymarket.domain.User;
import com.thedaymarket.repository.RatingRepository;
import com.thedaymarket.service.RatingService;
import com.thedaymarket.service.events.RatingChangedEvent;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class RatingServiceImpl implements RatingService {

  private final RatingRepository ratingRepository;
  private final ApplicationEventPublisher eventPublisher;

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

    var savedRating = ratingRepository.save(rating);

    eventPublisher.publishEvent(new RatingChangedEvent(this, savedRating));

    return savedRating;
  }

  @Override
  public Optional<Rating> getRatingByRater(RatingType type, User rater, Long receiverId) {
    return ratingRepository.findByTypeAndRaterAndReceiverId(type, rater, receiverId);
  }

  @Override
  public int getAverageRating(RatingType type, Long receiverId) {
    var ratings = ratingRepository.findByTypeAndReceiverId(type, receiverId);

    if (!ratings.isEmpty()) {
      var totalRating = ratings.stream().map(Rating::getStars).reduce(0.0f, Float::sum);
      var averageRating = Math.ceil(totalRating / ratings.size());
      return Math.min((int) averageRating, 5);
    }

    return 0;
  }
}
