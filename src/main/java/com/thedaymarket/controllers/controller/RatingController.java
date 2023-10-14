package com.thedaymarket.controllers.controller;

import com.thedaymarket.controllers.request.RatingRequest;
import com.thedaymarket.controllers.response.RatingResponse;
import com.thedaymarket.domain.Rating;
import com.thedaymarket.service.RatingService;
import com.thedaymarket.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1/ratings")
public class RatingController {

  private RatingService ratingService;
  private UserService userService;

  @PostMapping
  public RatingResponse addRating(@RequestBody RatingRequest rating) {
    var rater = userService.getUser(rating.raterId());
    var savedRating =
        ratingService.addRating(rating.stars(), rating.type(), rater, rating.receiverId());
    return RatingResponse.of(savedRating);
  }
}
