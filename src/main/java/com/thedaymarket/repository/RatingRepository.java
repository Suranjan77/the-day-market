package com.thedaymarket.repository;

import com.thedaymarket.domain.Rating;
import com.thedaymarket.domain.RatingType;
import com.thedaymarket.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {
  Optional<Rating> findByTypeAndRaterAndReceiverId(RatingType type, User rater, Long receiverId);
  List<Rating> findByTypeAndReceiverId(RatingType type, Long receiverId);
}
